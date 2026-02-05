package com.goflow.app.onboarding.integration

import android.content.Context
import androidx.wear.tiles.TileService
import dagger.hilt.android.qualifiers.ActivityContext
import com.goflow.app.common.data.integration.DeviceRegistration
import com.goflow.app.common.data.servers.ServerManager
import com.goflow.app.common.util.AppVersionProvider
import com.goflow.app.common.util.MessagingTokenProvider
import com.goflow.app.database.server.TemporaryServer
import com.goflow.app.tiles.CameraTile
import com.goflow.app.tiles.ConversationTile
import com.goflow.app.tiles.ShortcutsTile
import com.goflow.app.tiles.TemplateTile
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

class MobileAppIntegrationPresenterImpl @Inject constructor(
    @ActivityContext context: Context,
    private val serverManager: ServerManager,
    private val appVersionProvider: AppVersionProvider,
    private val messagingTokenProvider: MessagingTokenProvider,
) : MobileAppIntegrationPresenter {
    private val view = context as MobileAppIntegrationView
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main + Job())

    private suspend fun createRegistration(deviceName: String): DeviceRegistration {
        return DeviceRegistration(
            appVersionProvider(),
            deviceName,
            messagingTokenProvider(),
            false,
        )
    }

    override fun onRegistrationAttempt(temporaryServer: TemporaryServer, deviceName: String) {
        view.showLoading()
        mainScope.launch {
            val deviceRegistration = createRegistration(deviceName)
            var serverId: Int? = null
            try {
                serverId = serverManager.addServer(temporaryServer)
                serverManager.integrationRepository(serverId).registerDevice(deviceRegistration)
            } catch (e: Exception) {
                Timber.e(e, "Unable to register with Home Assistant")
                if (serverId != null) {
                    serverManager.authenticationRepository(serverId).revokeSession()
                    serverManager.removeServer(serverId)
                }
                view.showError()
                return@launch
            }
            updateTiles()
            view.deviceRegistered()
        }
    }

    private fun updateTiles() = mainScope.launch {
        try {
            val context = view as Context
            val updater = TileService.getUpdater(context)
            updater.requestUpdate(CameraTile::class.java)
            updater.requestUpdate(ConversationTile::class.java)
            updater.requestUpdate(ShortcutsTile::class.java)
            updater.requestUpdate(TemplateTile::class.java)
        } catch (e: Exception) {
            Timber.w("Unable to request tiles update")
        }
    }

    override fun onFinish() {
        mainScope.cancel()
    }
}
