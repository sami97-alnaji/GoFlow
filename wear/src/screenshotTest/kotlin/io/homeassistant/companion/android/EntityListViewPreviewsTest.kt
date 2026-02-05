package com.goflow.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.tooling.preview.devices.WearDevices
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.R
import com.goflow.app.home.views.EntityViewList
import com.goflow.app.util.previewEntity1
import com.goflow.app.util.previewEntity2

class EntityListViewPreviewsTest {

    @PreviewTest
    @Preview(device = WearDevices.LARGE_ROUND)
    @Composable
    private fun PreviewEntityListView() {
        EntityViewList(
            entityLists = mapOf(stringResource(R.string.lights) to listOf(previewEntity1, previewEntity2)),
            entityListsOrder = listOf(stringResource(R.string.lights)),
            entityListFilter = { true },
            onEntityClicked = { _, _ -> },
            onEntityLongClicked = { },
            isHapticEnabled = false,
            isToastEnabled = false,
        )
    }
}
