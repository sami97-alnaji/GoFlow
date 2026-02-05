package com.goflow.app.compose.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.composable.HABanner
import com.goflow.app.common.compose.composable.HAHint
import com.goflow.app.common.compose.theme.HAThemeForPreview

class HABannerScreenshotTest {

    @PreviewLightDark
    @PreviewTest
    @Composable
    fun `Simple HABanner with content`() {
        HAThemeForPreview {
            HABanner {
                Text("Simple content")
            }
        }
    }

    @PreviewLightDark
    @PreviewTest
    @Composable
    fun `HAHint with content not closable`() {
        HAThemeForPreview {
            HAHint(
                "Simple content, but quite long to see how it behaves on the width. It should be on multiples lines.",
            )
        }
    }

    @PreviewLightDark
    @PreviewTest
    @Composable
    fun `HAHint with content closable`() {
        HAThemeForPreview {
            HAHint(
                "Simple content.",
                onClose = {},
            )
        }
    }
}
