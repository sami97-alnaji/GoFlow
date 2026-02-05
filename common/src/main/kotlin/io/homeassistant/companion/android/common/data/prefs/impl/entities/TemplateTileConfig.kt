package com.goflow.app.common.data.prefs.impl.entities

import kotlinx.serialization.Serializable

@Serializable
data class TemplateTileConfig(val template: String, val refreshInterval: Int)
