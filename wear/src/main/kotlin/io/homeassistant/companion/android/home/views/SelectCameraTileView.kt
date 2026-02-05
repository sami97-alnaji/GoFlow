package com.goflow.app.home.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.goflow.app.common.R as commonR
import com.goflow.app.database.wear.CameraTile
import com.goflow.app.theme.WearAppTheme
import com.goflow.app.theme.getFilledTonalButtonColors
import com.goflow.app.views.ListHeader
import com.goflow.app.views.ThemeLazyColumn

@Composable
fun SelectCameraTileView(tiles: List<CameraTile>, onSelectTile: (tileId: Int) -> Unit) {
    WearAppTheme {
        ThemeLazyColumn {
            item {
                ListHeader(id = commonR.string.camera_tiles)
            }
            if (tiles.isEmpty()) {
                item {
                    Text(
                        text = stringResource(commonR.string.camera_tile_no_tiles_yet),
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                itemsIndexed(tiles, key = { _, item -> "tile.${item.id}" }) { index, tile ->
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(commonR.string.camera_tile_n, index + 1)) },
                        secondaryLabel = if (tile.entityId != null) {
                            { Text(tile.entityId!!) }
                        } else {
                            null
                        },
                        onClick = { onSelectTile(tile.id) },
                        colors = getFilledTonalButtonColors(),
                    )
                }
            }
        }
    }
}

@Preview(device = WearDevices.LARGE_ROUND)
@Composable
private fun PreviewSelectCameraTileViewOne() {
    SelectCameraTileView(
        tiles = listOf(
            CameraTile(id = 1, entityId = "camera.buienradar", refreshInterval = 300),
        ),
        onSelectTile = {},
    )
}

@Preview(device = WearDevices.LARGE_ROUND)
@Composable
private fun PreviewSelectCameraTileViewEmpty() {
    SelectCameraTileView(tiles = emptyList(), onSelectTile = {})
}
