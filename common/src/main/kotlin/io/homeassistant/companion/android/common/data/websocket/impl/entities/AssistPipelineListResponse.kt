package com.goflow.app.common.data.websocket.impl.entities

import kotlinx.serialization.Serializable

@Serializable
data class AssistPipelineListResponse(
    val pipelines: List<AssistPipelineResponse>,
    val preferredPipeline: String? = null,
)
