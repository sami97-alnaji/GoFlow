package com.goflow.app.common.data.integration.impl.entities

import com.goflow.app.common.util.MapAnySerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class ActionRequest(
    val domain: String,
    val service: String,
    @Serializable(with = MapAnySerializer::class)
    val serviceData: Map<String, @Polymorphic Any?>,
)
