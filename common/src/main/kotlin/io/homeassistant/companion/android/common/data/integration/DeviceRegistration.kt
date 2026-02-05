package com.goflow.app.common.data.integration

import com.goflow.app.common.util.AppVersion
import com.goflow.app.common.util.MessagingToken
import javax.inject.Qualifier

data class DeviceRegistration(
    val appVersion: AppVersion? = null,
    val deviceName: String? = null,
    var pushToken: MessagingToken? = null,
    var pushWebsocket: Boolean = true,
)

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PushWebsocketSupport
