package com.goflow.app.common.data.integration

import com.goflow.app.common.util.AnySerializer
import kotlinx.serialization.Serializable

@Serializable
data class ActionFields(
    val name: String? = null,
    val description: String? = null,
    @Serializable(with = AnySerializer::class)
    val example: Any? = null,
    val values: List<String>? = null,
)
