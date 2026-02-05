package com.goflow.app.common.data.websocket.impl.entities

import kotlinx.serialization.Serializable

@Serializable
data class CurrentUserResponse(val id: String, val name: String, val isOwner: Boolean, val isAdmin: Boolean)
