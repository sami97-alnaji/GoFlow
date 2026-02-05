package com.goflow.app.util

import com.goflow.app.common.data.integration.Entity
import com.goflow.app.database.notification.NotificationItem
import com.goflow.app.database.server.Server
import com.goflow.app.database.server.ServerConnectionInfo
import com.goflow.app.database.server.ServerSessionInfo
import com.goflow.app.database.server.ServerUserInfo
import java.time.LocalDateTime

val notificationItem = NotificationItem(1, 1636389288682, "testing", "{\"message\":\"test\"}", "FCM", null)

val wearDeviceName = "Device Name"

val attributes: Map<String, *> = mapOf(
    "friendly_name" to "Testing",
    "icon" to "mdi:cellphone",
)

private val localDateTime: LocalDateTime = LocalDateTime.now()

val previewEntity1 = Entity("light.test", "on", attributes, localDateTime, localDateTime)
val previewEntity2 = Entity("scene.testing", "on", attributes, localDateTime, localDateTime)

val previewServer1 =
    Server(
        id = 0,
        _name = "Home",
        listOrder = -1,
        connection = ServerConnectionInfo(externalUrl = ""),
        session = ServerSessionInfo(),
        user = ServerUserInfo(),
    )
val previewServer2 =
    Server(
        id = 1,
        _name = "Friends home",
        listOrder = -1,
        connection = ServerConnectionInfo(externalUrl = ""),
        session = ServerSessionInfo(),
        user = ServerUserInfo(),
    )
