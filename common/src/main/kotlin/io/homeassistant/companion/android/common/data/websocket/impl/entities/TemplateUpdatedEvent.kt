package com.goflow.app.common.data.websocket.impl.entities

import com.goflow.app.common.util.MapAnySerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class TemplateUpdatedEvent(
    val result: String? = null,
    @Serializable(with = MapAnySerializer::class)
    val listeners: Map<String, @Polymorphic Any?>,
)
