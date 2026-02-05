package com.goflow.app.onboarding.localfirst

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.theme.HAThemeForPreview
import com.goflow.app.util.compose.HAPreviews

class LocalFirstScreenshotTest {

    @PreviewTest
    @HAPreviews
    @Composable
    fun `LocalFirstContent empty`() {
        HAThemeForPreview {
            LocalFirstScreen(onNextClick = {})
        }
    }
}
