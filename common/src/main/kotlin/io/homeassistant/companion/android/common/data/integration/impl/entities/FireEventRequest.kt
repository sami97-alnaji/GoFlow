package com.goflow.app.common.data.integration.impl.entities

import com.goflow.app.common.util.MapAnySerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class FireEventRequest(
    val eventType: String,
    @Serializable(with = MapAnySerializer::class)
    val eventData: Map<String, @Polymorphic Any?>,
)
