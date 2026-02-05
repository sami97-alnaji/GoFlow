package com.goflow.app.util.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.theme.HAThemeForPreview

class HAAppScreenshotTest {

    @PreviewTest
    @HAPreviews
    @Composable
    fun `HAApp no start destination shows loading screen`() {
        HAThemeForPreview {
            HAApp(navController = rememberNavController(), startDestination = null)
        }
    }
}
