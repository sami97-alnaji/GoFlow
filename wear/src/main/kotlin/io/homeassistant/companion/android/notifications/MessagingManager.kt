package com.goflow.app.notifications

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import com.goflow.app.common.data.servers.ServerManager
import com.goflow.app.common.notifications.DeviceCommandData
import com.goflow.app.common.notifications.NotificationData
import com.goflow.app.common.notifications.clearNotification
import com.goflow.app.common.notifications.commandBeaconMonitor
import com.goflow.app.common.notifications.commandBleTransmitter
import com.goflow.app.common.notifications.getGroupNotificationBuilder
import com.goflow.app.common.notifications.handleChannel
import com.goflow.app.common.notifications.handleDeleteIntent
import com.goflow.app.common.notifications.handleSmallIcon
import com.goflow.app.common.notifications.handleText
import com.goflow.app.common.util.cancelGroupIfNeeded
import com.goflow.app.common.util.getActiveNotification
import com.goflow.app.common.util.toJsonObject
import com.goflow.app.common.util.tts.TextToSpeechClient
import com.goflow.app.common.util.tts.TextToSpeechData
import com.goflow.app.database.notification.NotificationDao
import com.goflow.app.database.notification.NotificationItem
import com.goflow.app.database.sensor.SensorDao
import com.goflow.app.sensors.SensorReceiver
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MessagingManager @Inject constructor(
    @ApplicationContext val context: Context,
    private val serverManager: ServerManager,
    private val sensorDao: SensorDao,
    private val notificationDao: NotificationDao,
    private val textToSpeechClient: TextToSpeechClient,
) {
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main + Job())

    fun handleMessage(notificationData: Map<String, String>, source: String) {
        mainScope.launch {
            val now = System.currentTimeMillis()

            val jsonData = notificationData as Map<String, String>?
            val jsonObject = jsonData?.toJsonObject()
            val serverId = jsonData?.get(NotificationData.WEBHOOK_ID)?.let {
                serverManager.getServer(webhookId = it)?.id
            } ?: ServerManager.SERVER_ID_ACTIVE
            val notificationRow =
                NotificationItem(
                    0,
                    now,
                    notificationData[NotificationData.MESSAGE].toString(),
                    jsonObject.toString(),
                    source,
                    serverId,
                )
            notificationDao.add(notificationRow)
            if (serverManager.getServer(serverId) == null) {
                Timber.w("Received notification but no server for it, discarding")
                return@launch
            }

            val allowCommands = serverManager.integrationRepository(serverId).isTrusted()
            val message = notificationData[NotificationData.MESSAGE]
            when {
                message == NotificationData.CLEAR_NOTIFICATION && !notificationData["tag"].isNullOrBlank() -> {
                    clearNotification(context, notificationData["tag"]!!)
                }
                message == DeviceCommandData.COMMAND_BEACON_MONITOR && allowCommands -> {
                    if (!commandBeaconMonitor(context, notificationData)) {
                        sendNotification(notificationData, now)
                    }
                }
                message == DeviceCommandData.COMMAND_BLE_TRANSMITTER && allowCommands -> {
                    if (!commandBleTransmitter(context, notificationData, sensorDao)) {
                        sendNotification(notificationData)
                    }
                }
                message == TextToSpeechData.TTS -> textToSpeechClient.speakText(notificationData)
                message == TextToSpeechData.COMMAND_STOP_TTS -> textToSpeechClient.stopTTS()
                message == DeviceCommandData.COMMAND_UPDATE_SENSORS -> SensorReceiver.updateAllSensors(context)
                else -> sendNotification(notificationData, now)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(data: Map<String, String>, received: Long? = null) {
        val notificationManagerCompat = NotificationManagerCompat.from(context)

        val tag = data["tag"].takeIf { !it.isNullOrBlank() }
        val messageId = tag?.hashCode() ?: received?.toInt() ?: System.currentTimeMillis().toInt()

        var group = data["group"]
        var groupId = 0
        var previousGroup = ""
        var previousGroupId = 0
        if (!group.isNullOrBlank()) {
            group = NotificationData.GROUP_PREFIX + group
            groupId = group.hashCode()
        } else {
            val notification = notificationManagerCompat.getActiveNotification(tag, messageId)
            if (notification != null && notification.isGroup) {
                previousGroup = NotificationData.GROUP_PREFIX + notification.tag
                previousGroupId = previousGroup.hashCode()
            }
        }

        val channelId = handleChannel(context, notificationManagerCompat, data)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)

        handleSmallIcon(context, notificationBuilder, data)

        handleText(notificationBuilder, data)

        handleDeleteIntent(context, notificationBuilder, data, messageId, group, groupId, null)

        notificationManagerCompat.apply {
            Timber.d("Show notification with tag \"$tag\" and id \"$messageId\"")
            notify(tag, messageId, notificationBuilder.build())
            if (!group.isNullOrBlank()) {
                Timber.d("Show group notification with tag \"$group\" and id \"$groupId\"")
                notify(group, groupId, getGroupNotificationBuilder(context, channelId, group, data).build())
            } else {
                if (previousGroup.isNotBlank()) {
                    Timber.d(
                        "Remove group notification with tag \"$previousGroup\" and id \"$previousGroupId\"",
                    )
                    notificationManagerCompat.cancelGroupIfNeeded(previousGroup, previousGroupId)
                }
            }
        }
    }
}
