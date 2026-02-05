package com.goflow.app.phone

import android.annotation.SuppressLint
import android.content.Intent
import androidx.wear.tiles.TileService
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import com.goflow.app.common.data.authentication.ServerRegistrationRepository
import com.goflow.app.common.data.integration.DeviceRegistration
import com.goflow.app.common.data.keychain.KeyChainRepository
import com.goflow.app.common.data.keychain.KeyStoreRepository
import com.goflow.app.common.data.keychain.NamedKeyChain
import com.goflow.app.common.data.keychain.NamedKeyStore
import com.goflow.app.common.data.prefs.WearPrefsRepository
import com.goflow.app.common.data.prefs.impl.entities.TemplateTileConfig
import com.goflow.app.common.data.servers.ServerManager
import com.goflow.app.common.util.AppVersionProvider
import com.goflow.app.common.util.MessagingTokenProvider
import com.goflow.app.common.util.WearDataMessages
import com.goflow.app.common.util.kotlinJsonMapper
import com.goflow.app.database.wear.FavoritesDao
import com.goflow.app.database.wear.getAll
import com.goflow.app.database.wear.replaceAll
import com.goflow.app.home.HomeActivity
import com.goflow.app.home.HomePresenterImpl
import com.goflow.app.tiles.CameraTile
import com.goflow.app.tiles.ConversationTile
import com.goflow.app.tiles.ShortcutsTile
import com.goflow.app.tiles.TemplateTile
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@AndroidEntryPoint
@SuppressLint("VisibleForTests") // https://issuetracker.google.com/issues/239451111
class PhoneSettingsListener :
    WearableListenerService(),
    DataClient.OnDataChangedListener {

    @Inject
    lateinit var serverManager: ServerManager

    @Inject
    lateinit var serverRegistrationRepository: ServerRegistrationRepository

    @Inject
    lateinit var wearPrefsRepository: WearPrefsRepository

    @Inject
    lateinit var favoritesDao: FavoritesDao

    @Inject
    @NamedKeyChain
    lateinit var keyChainRepository: KeyChainRepository

    @Inject
    @NamedKeyStore
    lateinit var keyStore: KeyChainRepository

    @Inject
    lateinit var appVersionProvider: AppVersionProvider

    @Inject
    lateinit var messagingTokenProvider: MessagingTokenProvider

    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onMessageReceived(event: MessageEvent) {
        Timber.d("Message received: $event")
        if (event.path == "/requestConfig") {
            sendPhoneData()
        }
    }

    private fun sendPhoneData() = mainScope.launch {
        val currentFavorites = favoritesDao.getAll()
        val putDataRequest = PutDataMapRequest.create("/config").run {
            dataMap.putLong(WearDataMessages.KEY_UPDATE_TIME, System.nanoTime())
            val isRegistered = serverManager.isRegistered()
            dataMap.putBoolean(WearDataMessages.CONFIG_IS_AUTHENTICATED, isRegistered)
            if (isRegistered) {
                dataMap.putInt(WearDataMessages.CONFIG_SERVER_ID, serverManager.getServer()?.id ?: 0)
                dataMap.putString(
                    WearDataMessages.CONFIG_SERVER_EXTERNAL_URL,
                    serverManager.getServer()?.connection?.externalUrl ?: "",
                )
                dataMap.putString(
                    WearDataMessages.CONFIG_SERVER_WEBHOOK_ID,
                    serverManager.getServer()?.connection?.webhookId ?: "",
                )
                dataMap.putString(
                    WearDataMessages.CONFIG_SERVER_CLOUD_URL,
                    serverManager.getServer()?.connection?.cloudUrl ?: "",
                )
                dataMap.putString(
                    WearDataMessages.CONFIG_SERVER_CLOUDHOOK_URL,
                    serverManager.getServer()?.connection?.cloudhookUrl ?: "",
                )
                dataMap.putBoolean(
                    WearDataMessages.CONFIG_SERVER_USE_CLOUD,
                    serverManager.getServer()?.connection?.useCloud ?: false,
                )
                dataMap.putString(
                    WearDataMessages.CONFIG_SERVER_REFRESH_TOKEN,
                    serverManager.getServer()?.session?.refreshToken ?: "",
                )
            }
            dataMap.putString(
                WearDataMessages.CONFIG_SUPPORTED_DOMAINS,
                kotlinJsonMapper.encodeToString(HomePresenterImpl.supportedDomains),
            )
            dataMap.putString(WearDataMessages.CONFIG_FAVORITES, kotlinJsonMapper.encodeToString(currentFavorites))
            dataMap.putString(
                WearDataMessages.CONFIG_TEMPLATE_TILES,
                kotlinJsonMapper.encodeToString(wearPrefsRepository.getAllTemplateTiles()),
            )
            setUrgent()
            asPutDataRequest()
        }

        try {
            Wearable.getDataClient(this@PhoneSettingsListener).putDataItem(putDataRequest).await()
            Timber.d("Successfully sent /config to device")
        } catch (e: Exception) {
            Timber.e(e, "Failed to send /config to device")
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Timber.d("onDataChanged ${dataEvents.count}")
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                event.dataItem.also { item ->
                    when (item.uri.path) {
                        "/authenticate" -> {
                            login(DataMapItem.fromDataItem(item).dataMap)
                        }

                        "/updateFavorites" -> {
                            saveFavorites(DataMapItem.fromDataItem(item).dataMap)
                        }

                        "/updateTemplateTiles" -> {
                            saveTemplateTiles(DataMapItem.fromDataItem(item).dataMap)
                        }
                    }
                }
            }
        }
        dataEvents.release()
    }

    private fun login(dataMap: DataMap) = mainScope.launch {
        var authId = ""
        var serverId: Int? = null
        try {
            authId = dataMap.getString("AuthId", "")
            val url = dataMap.getString("URL", "")
            val authCode = dataMap.getString("AuthCode", "")
            val deviceName = dataMap.getString("DeviceName")
            val deviceTrackingEnabled = dataMap.getBoolean("LocationTracking")
            val notificationsEnabled = dataMap.getBoolean("Notifications")
            val tlsClientCertificateData = dataMap.getByteArray("TLSClientCertificateData")
            val tlsClientCertificatePassword = dataMap.getString("TLSClientCertificatePassword").orEmpty().toCharArray()

            // load TLS key
            if (tlsClientCertificateData != null && tlsClientCertificateData.isNotEmpty()) {
                KeyStore.getInstance("PKCS12").apply {
                    load(tlsClientCertificateData.inputStream(), tlsClientCertificatePassword)

                    val alias = aliases().nextElement()
                    val certificateChain = getCertificateChain(alias).filterIsInstance<X509Certificate>().toTypedArray()
                    val privateKey = getKey(alias, tlsClientCertificatePassword) as PrivateKey

                    // we store the TLS Client key under a static alias because there is currently
                    // no way to ask the user for the correct alias
                    keyStore.setData(KeyStoreRepository.ALIAS, privateKey, certificateChain)
                    keyChainRepository.load(applicationContext)
                }
            }

            val temporaryServer = checkNotNull(
                serverRegistrationRepository.registerAuthorizationCode(
                    url,
                    authCode,
                    null,
                ),
            ) { "Registration failed" }

            serverId = serverManager.addServer(temporaryServer)
            serverManager.integrationRepository(serverId).registerDevice(
                DeviceRegistration(
                    appVersionProvider(),
                    deviceName,
                    messagingTokenProvider(),
                    false,
                ),
            )
            launch {
                sendLoginResult(authId, true, null)
                updateTiles()
            }

            val intent = HomeActivity.newInstance(applicationContext, fromOnboarding = true)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e, "Unable to login to Home Assistant")
            try {
                if (serverId != null) {
                    serverManager.authenticationRepository(serverId).revokeSession()
                    serverManager.removeServer(serverId)
                }
            } catch (e: Exception) {
                Timber.e(e, "Can't revoke session")
            }
            launch {
                sendLoginResult(authId, false, e.stackTraceToString())
            }
        }

        sendPhoneData()
    }

    private suspend fun sendLoginResult(id: String?, success: Boolean, exception: String?) {
        try {
            val putDataRequest = PutDataMapRequest.create(WearDataMessages.PATH_LOGIN_RESULT).run {
                dataMap.putString(WearDataMessages.KEY_ID, id ?: "")
                dataMap.putBoolean(WearDataMessages.KEY_SUCCESS, success)
                if (exception != null) {
                    dataMap.putString(WearDataMessages.LOGIN_RESULT_EXCEPTION, exception)
                }
                setUrgent()
                asPutDataRequest()
            }
            Wearable.getDataClient(this@PhoneSettingsListener).putDataItem(putDataRequest).await()
            Timber.d("Successfully sent ${WearDataMessages.PATH_LOGIN_RESULT} to device")
        } catch (e: Exception) {
            Timber.w(e, "Failed to send ${WearDataMessages.PATH_LOGIN_RESULT} to device")
        }
    }

    private fun saveFavorites(dataMap: DataMap) {
        val favoritesIds: List<String> =
            kotlinJsonMapper.decodeFromString(dataMap.getString(WearDataMessages.CONFIG_FAVORITES, "[]"))

        mainScope.launch {
            favoritesDao.replaceAll(favoritesIds)

            if (favoritesIds.isEmpty() && wearPrefsRepository.getWearFavoritesOnly()) {
                wearPrefsRepository.setWearFavoritesOnly(false)
            }
        }
    }

    private fun saveTemplateTiles(dataMap: DataMap) = mainScope.launch {
        val templateTilesFromPhone: Map<Int, TemplateTileConfig> = kotlinJsonMapper.decodeFromString(
            dataMap.getString(
                WearDataMessages.CONFIG_TEMPLATE_TILES,
                "{}",
            ),
        )

        wearPrefsRepository.setAllTemplateTiles(templateTilesFromPhone)
    }

    private fun updateTiles() = mainScope.launch {
        try {
            val updater = TileService.getUpdater(applicationContext)
            updater.requestUpdate(CameraTile::class.java)
            updater.requestUpdate(ConversationTile::class.java)
            updater.requestUpdate(ShortcutsTile::class.java)
            updater.requestUpdate(TemplateTile::class.java)
        } catch (e: Exception) {
            Timber.w(e, "Unable to request tiles update")
        }
    }
}
