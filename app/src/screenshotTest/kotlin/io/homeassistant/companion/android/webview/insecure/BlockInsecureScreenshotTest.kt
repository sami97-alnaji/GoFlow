package com.goflow.app.webview.insecure

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.theme.HAThemeForPreview
import com.goflow.app.util.compose.HAPreviews

class BlockInsecureScreenshotTest {

    @PreviewTest
    @HAPreviews
    @Composable
    fun `BlockInsecure both missing`() {
        HAThemeForPreview {
            BlockInsecureScreen(
                missingHomeSetup = true,
                missingLocation = true,
                onRetry = {},
                onHelpClick = {},
                onOpenSettings = {},
                onChangeSecurityLevel = {},
                onOpenLocationSettings = {},
                onConfigureHomeNetwork = {},
            )
        }
    }

    @PreviewTest
    @HAPreviews
    @Composable
    fun `BlockInsecure missing location only`() {
        HAThemeForPreview {
            BlockInsecureScreen(
                missingHomeSetup = false,
                missingLocation = true,
                onRetry = {},
                onHelpClick = {},
                onOpenSettings = {},
                onChangeSecurityLevel = {},
                onOpenLocationSettings = {},
                onConfigureHomeNetwork = {},
            )
        }
    }

    @PreviewTest
    @HAPreviews
    @Composable
    fun `BlockInsecure missing home setup only`() {
        HAThemeForPreview {
            BlockInsecureScreen(
                missingHomeSetup = true,
                missingLocation = false,
                onRetry = {},
                onHelpClick = {},
                onOpenSettings = {},
                onChangeSecurityLevel = {},
                onOpenLocationSettings = {},
                onConfigureHomeNetwork = {},
            )
        }
    }

    @PreviewTest
    @HAPreviews
    @Composable
    fun `BlockInsecure no missing`() {
        HAThemeForPreview {
            BlockInsecureScreen(
                missingHomeSetup = false,
                missingLocation = false,
                onRetry = {},
                onHelpClick = {},
                onOpenSettings = {},
                onChangeSecurityLevel = {},
                onOpenLocationSettings = {},
                onConfigureHomeNetwork = {},
            )
        }
    }
}
