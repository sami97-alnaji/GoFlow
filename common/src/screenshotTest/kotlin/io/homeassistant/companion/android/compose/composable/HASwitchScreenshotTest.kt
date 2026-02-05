package com.goflow.app.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.composable.HASwitch
import com.goflow.app.common.compose.theme.HAThemeForPreview

class HASwitchScreenshotTest {
    @PreviewLightDark
    @PreviewTest
    @Composable
    fun `HASwitch`() {
        HAThemeForPreview {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Row {
                    HASwitch(true, onCheckedChange = {})
                    HASwitch(checked = false, onCheckedChange = {})
                }
                Row {
                    HASwitch(enabled = false, checked = true, onCheckedChange = {})
                    HASwitch(enabled = false, checked = false, onCheckedChange = {})
                }
            }
        }
    }
}
