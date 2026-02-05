package com.goflow.app.onboarding.sethomenetwork

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.theme.HAThemeForPreview
import com.goflow.app.util.compose.HAPreviews

class SetHomeNetworkScreenshotTest {
    @PreviewTest
    @HAPreviews
    @Composable
    fun `SetHomeNetworkScreen with VPN and Ethernet`() {
        HAThemeForPreview {
            SetHomeNetworkScreen(
                onHelpClick = {},
                showEthernet = true,
                showVpn = true,
                isUsingVpn = true,
                isUsingEthernet = true,
                currentWifiNetwork = "HomeAssistant-5G",
                onCurrentWifiNetworkChange = {},
                onUsingVpnChange = {},
                onUsingEthernetChange = {},
                onNextClick = {},
            )
        }
    }

    @PreviewTest
    @HAPreviews
    @Composable
    fun `SetHomeNetworkScreen with no VPN and no Ethernet`() {
        HAThemeForPreview {
            SetHomeNetworkScreen(
                onHelpClick = {},
                showEthernet = false,
                showVpn = false,
                isUsingVpn = false,
                isUsingEthernet = false,
                currentWifiNetwork = "",
                onCurrentWifiNetworkChange = {},
                onUsingVpnChange = {},
                onUsingEthernetChange = {},
                onNextClick = {},
            )
        }
    }
}
