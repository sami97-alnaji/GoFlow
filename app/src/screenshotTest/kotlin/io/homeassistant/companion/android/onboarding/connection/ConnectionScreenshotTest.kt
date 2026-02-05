package com.goflow.app.onboarding.connection

import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.theme.HAThemeForPreview
import com.goflow.app.util.compose.HAPreviews

class ConnectionScreenshotTest {

    @PreviewTest
    @HAPreviews
    @Composable
    fun `ConnectionScreen loading`() {
        HAThemeForPreview {
            ConnectionScreen(
                url = "https://www.example.com",
                isLoading = true,
                isError = false,
                webViewClient = WebViewClient(),
                onBackClick = {},
            )
        }
    }

    @PreviewTest
    @HAPreviews
    @Composable
    fun `ConnectionScreen loaded`() {
        HAThemeForPreview {
            ConnectionScreen(
                url = "https://www.example.com",
                isLoading = false,
                isError = false,
                webViewClient = WebViewClient(),
                onBackClick = {},
            )
        }
    }

    @PreviewTest
    @HAPreviews
    @Composable
    fun `ConnectionScreen error`() {
        HAThemeForPreview {
            ConnectionScreen(
                url = "https://www.example.com",
                isLoading = false,
                isError = true,
                webViewClient = WebViewClient(),
                onBackClick = {},
            )
        }
    }
}
