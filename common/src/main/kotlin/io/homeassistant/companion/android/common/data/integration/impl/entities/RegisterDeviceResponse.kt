package com.goflow.app.common.data.integration.impl.entities

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceResponse(
    val cloudhookUrl: String?,
    val remoteUiUrl: String?,
    val secret: String?,
    val webhookId: String,
)
