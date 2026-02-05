package com.goflow.app.widgets.todo

import com.goflow.app.common.data.integration.Entity
import java.time.LocalDateTime

internal fun fakeServerEntity(entityId: String, friendlyName: String? = null): Entity {
    return Entity(
        entityId = entityId,
        state = "",
        attributes = if (friendlyName != null) mapOf("friendly_name" to friendlyName) else emptyMap(),
        lastChanged = LocalDateTime.now(),
        lastUpdated = LocalDateTime.now(),
    )
}
