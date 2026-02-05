package com.goflow.app.common.data.websocket.impl.entities

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse<T>(val eventType: String, val timeFired: String, val data: T)
