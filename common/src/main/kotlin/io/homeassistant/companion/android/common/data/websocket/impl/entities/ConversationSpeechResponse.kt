package com.goflow.app.common.data.websocket.impl.entities

import com.goflow.app.common.util.AnySerializer
import com.goflow.app.common.util.MapAnySerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class ConversationSpeechResponse(
    val speech: ConversationSpeechPlainResponse? = null,
    @Serializable(with = AnySerializer::class)
    val card: Any? = null,
    val language: String? = null,
    val responseType: String? = null,
    @Serializable(with = MapAnySerializer::class)
    val data: Map<String, @Polymorphic Any?>? = null,
)
