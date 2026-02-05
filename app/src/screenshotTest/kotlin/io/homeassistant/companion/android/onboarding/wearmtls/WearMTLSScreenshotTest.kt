package com.goflow.app.onboarding.wearmtls

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.theme.HAThemeForPreview
import com.goflow.app.util.compose.HAPreviews

class WearMTLSScreenshotTest {
    @PreviewTest
    @HAPreviews
    @Composable
    fun `WearMTLSScreen no file selected`() {
        HAThemeForPreview {
            WearMTLSScreen(
                onHelpClick = {},
                onBackClick = {},
                onNext = { _, _ -> },
                onPasswordChange = {},
                onFileChange = {},
                selectedUri = null,
                selectedFilename = null,
                currentPassword = "",
                isCertValidated = false,
                isError = false,
            )
        }
    }

    @PreviewTest
    @HAPreviews
    @Composable
    fun `WearMTLSScreen file selected`() {
        HAThemeForPreview {
            WearMTLSScreen(
                onHelpClick = {},
                onBackClick = {},
                onNext = { _, _ -> },
                onPasswordChange = {},
                onFileChange = {},
                selectedUri = null,
                selectedFilename = "Secret_cert.pem",
                currentPassword = "1234",
                isCertValidated = true,
                isError = false,
            )
        }
    }
}
