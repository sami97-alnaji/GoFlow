package com.goflow.app.common.data.websocket.impl.entities

import com.goflow.app.common.util.MapAnySerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class EntityRegistryUpdatedEvent(
    val action: String,
    val entityId: String,
    @Serializable(with = MapAnySerializer::class)
    val changes: Map<String, @Polymorphic Any?>? = null,
    val oldEntityId: String? = null,
)
