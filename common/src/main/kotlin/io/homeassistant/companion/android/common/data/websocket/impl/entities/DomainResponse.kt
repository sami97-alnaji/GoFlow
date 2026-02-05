package com.goflow.app.common.data.websocket.impl.entities

import com.goflow.app.common.data.integration.ActionData
import kotlinx.serialization.Serializable

@Serializable
data class DomainResponse(val domain: String, val services: Map<String, ActionData>)
