package com.goflow.app.common.data.integration.impl.entities

import kotlinx.serialization.Serializable

@Serializable
data class RateLimitRequest(val pushToken: String)
