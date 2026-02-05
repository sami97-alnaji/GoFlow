package com.goflow.app.common.data.websocket.impl.entities

import kotlinx.serialization.Serializable

@Serializable
data class AreaRegistryUpdatedEvent(val action: String, val areaId: String)
