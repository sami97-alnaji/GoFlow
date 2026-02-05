package com.goflow.app.onboarding.welcome

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.theme.HAThemeForPreview
import com.goflow.app.util.compose.HAPreviews

class WelcomeScreenshotTest {

    @PreviewTest
    @HAPreviews
    @Composable
    fun `WelcomeScreen`() {
        HAThemeForPreview {
            WelcomeScreen(onConnectClick = {}, onLearnMoreClick = {})
        }
    }
}
