package com.goflow.app.home.views

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.goflow.app.common.R as commonR
import com.goflow.app.common.data.integration.Entity
import com.goflow.app.theme.WearAppTheme
import com.goflow.app.theme.getPrimaryButtonColors
import com.goflow.app.util.playPreviewEntityScene1
import com.goflow.app.util.playPreviewEntityScene2
import com.goflow.app.util.playPreviewEntityScene3
import com.goflow.app.util.previewEntity1
import com.goflow.app.util.previewEntity2
import com.goflow.app.views.ExpandableListHeader
import com.goflow.app.views.ListHeader
import com.goflow.app.views.ThemeLazyColumn
import com.goflow.app.views.rememberExpandedStates

@Composable
fun EntityViewList(
    entityLists: Map<String, List<Entity>>,
    entityListsOrder: List<String>,
    entityListFilter: (Entity) -> Boolean,
    onEntityClicked: (String, String) -> Unit,
    onEntityLongClicked: (String) -> Unit,
    isHapticEnabled: Boolean,
    isToastEnabled: Boolean,
) {
    // Remember expanded state of each header
    val expandedStates = rememberExpandedStates(entityLists.keys.map { it.hashCode() })

    WearAppTheme {
        ThemeLazyColumn {
            for (header in entityListsOrder) {
                val entities = entityLists[header].orEmpty()
                if (entities.isNotEmpty()) {
                    item {
                        if (entityLists.size > 1) {
                            ExpandableListHeader(
                                string = header,
                                key = header.hashCode(),
                                expandedStates = expandedStates,
                            )
                        } else {
                            ListHeader(header)
                        }
                    }
                    if (expandedStates[header.hashCode()]!!) {
                        val filtered = entities.filter { entityListFilter(it) }
                        items(filtered, key = { it.entityId }) { entity ->
                            EntityUi(
                                entity,
                                onEntityClicked,
                                isHapticEnabled,
                                isToastEnabled,
                            ) { entityId -> onEntityLongClicked(entityId) }
                        }

                        if (filtered.isEmpty()) {
                            item {
                                Column {
                                    Button(
                                        label = {
                                            Text(
                                                text = stringResource(commonR.string.loading_entities),
                                                textAlign = TextAlign.Center,
                                            )
                                        },
                                        onClick = { /* No op */ },
                                        colors = getPrimaryButtonColors(),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = WearDevices.LARGE_ROUND)
@Composable
private fun PreviewEntityListView() {
    EntityViewList(
        entityLists = mapOf(stringResource(commonR.string.lights) to listOf(previewEntity1, previewEntity2)),
        entityListsOrder = listOf(stringResource(commonR.string.lights)),
        entityListFilter = { true },
        onEntityClicked = { _, _ -> },
        onEntityLongClicked = { },
        isHapticEnabled = false,
        isToastEnabled = false,
    )
}

@Preview(device = WearDevices.LARGE_ROUND)
@Composable
private fun PreviewEntityListScenes() {
    EntityViewList(
        entityLists = mapOf(
            stringResource(commonR.string.scenes) to
                listOf(playPreviewEntityScene1, playPreviewEntityScene2, playPreviewEntityScene3),
        ),
        entityListsOrder = listOf(stringResource(commonR.string.scenes)),
        entityListFilter = { true },
        onEntityClicked = { _, _ -> },
        onEntityLongClicked = { },
        isHapticEnabled = false,
        isToastEnabled = false,
    )
}

@Preview(device = WearDevices.LARGE_ROUND)
@Composable
private fun PreviewEntityListEmpty() {
    EntityViewList(
        entityLists = mapOf(stringResource(commonR.string.scenes) to listOf(playPreviewEntityScene1)),
        entityListsOrder = listOf(stringResource(commonR.string.scenes)),
        entityListFilter = { false },
        onEntityClicked = { _, _ -> },
        onEntityLongClicked = { },
        isHapticEnabled = false,
        isToastEnabled = false,
    )
}
