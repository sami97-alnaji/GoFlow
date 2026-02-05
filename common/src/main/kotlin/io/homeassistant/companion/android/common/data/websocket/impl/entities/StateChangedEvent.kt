package com.goflow.app.common.data.websocket.impl.entities

import com.goflow.app.common.data.integration.Entity
import kotlinx.serialization.Serializable

@Serializable
data class StateChangedEvent(val entityId: String, val oldState: Entity? = null, val newState: Entity? = null)
