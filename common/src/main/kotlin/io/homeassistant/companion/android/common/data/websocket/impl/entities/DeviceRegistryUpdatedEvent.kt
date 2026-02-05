package com.goflow.app.common.data.websocket.impl.entities

import kotlinx.serialization.Serializable

@Serializable
data class DeviceRegistryUpdatedEvent(val action: String, val deviceId: String)
