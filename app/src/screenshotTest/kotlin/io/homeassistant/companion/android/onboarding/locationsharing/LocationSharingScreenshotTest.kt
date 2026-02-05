package com.goflow.app.onboarding.locationsharing

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.theme.HAThemeForPreview
import com.goflow.app.util.compose.HAPreviews

class LocationSharingScreenshotTest {

    @PreviewTest
    @HAPreviews
    @Composable
    fun `LocationSharing empty`() {
        HAThemeForPreview {
            LocationSharingScreen(
                onHelpClick = {},
                onGoToNextScreen = {},
                onLocationSharingResponse = {},
            )
        }
    }
}
