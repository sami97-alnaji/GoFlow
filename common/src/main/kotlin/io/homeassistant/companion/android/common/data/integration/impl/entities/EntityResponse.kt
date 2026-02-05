package com.goflow.app.common.data.integration.impl.entities

import com.goflow.app.common.util.LocalDateTimeSerializer
import com.goflow.app.common.util.MapAnySerializer
import java.time.LocalDateTime
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class EntityResponse(
    val entityId: String,
    val state: String,
    @Serializable(with = MapAnySerializer::class)
    val attributes: Map<String, @Polymorphic Any?>,
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastChanged: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastUpdated: LocalDateTime,
)
