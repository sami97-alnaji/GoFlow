package com.goflow.app.compose.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.composable.HADetails
import com.goflow.app.common.compose.theme.HATextStyle
import com.goflow.app.common.compose.theme.HAThemeForPreview

class HADetailsScreenshotTest {

    @PreviewLightDark
    @PreviewTest
    @Composable
    fun `HADetails collapsed`() {
        HAThemeForPreview {
            HADetails("Hello world", defaultExpanded = false) {
                Text("This text should not be displayed")
            }
        }
    }

    @PreviewLightDark
    @PreviewTest
    @Composable
    fun `HADetails expanded`() {
        HAThemeForPreview {
            HADetails("Hello world", defaultExpanded = true) {
                Text("Nice little text", style = HATextStyle.Body)
            }
        }
    }
}
