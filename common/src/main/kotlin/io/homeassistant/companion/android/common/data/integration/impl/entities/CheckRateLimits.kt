package com.goflow.app.common.data.integration.impl.entities

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class CheckRateLimits(
    val target: String,
    @JsonNames("rateLimits")
    val rateLimits: RateLimitResponse,
)
