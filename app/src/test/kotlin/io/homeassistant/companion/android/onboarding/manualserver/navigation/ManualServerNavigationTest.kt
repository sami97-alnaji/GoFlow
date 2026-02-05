package com.goflow.app.onboarding.manualserver.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import com.goflow.app.common.R as commonR
import com.goflow.app.onboarding.BaseOnboardingNavigationTest
import com.goflow.app.onboarding.URL_GETTING_STARTED_DOCUMENTATION
import com.goflow.app.onboarding.connection.CONNECTION_SCREEN_TAG
import com.goflow.app.onboarding.connection.navigation.ConnectionRoute
import com.goflow.app.onboarding.serverdiscovery.navigation.ServerDiscoveryRoute
import com.goflow.app.onboarding.serverdiscovery.navigation.navigateToServerDiscovery
import com.goflow.app.testing.unit.stringResource
import com.goflow.app.util.compose.navigateToUri
import io.mockk.coVerify
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Navigation tests for the Manual Server screen in the onboarding flow.
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
internal class ManualServerNavigationTest : BaseOnboardingNavigationTest() {

    @Test
    fun `Given enter manual address when setting url and clicking connect then show ConnectScreen then back goes to ManualServer`() {
        testNavigation {
            navController.navigateToServerDiscovery()
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<ServerDiscoveryRoute>() == true)
            onNodeWithText(stringResource(commonR.string.manual_setup))
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<ManualServerRoute>() == true)

            onNodeWithContentDescription(stringResource(commonR.string.get_help)).performClick()
            coVerify { any<NavController>().navigateToUri(URL_GETTING_STARTED_DOCUMENTATION, any()) }

            onNodeWithText("http://homeassistant.local:8123").performTextInput("http://ha.local")

            onNodeWithText(stringResource(commonR.string.connect))
                .performScrollTo()
                .assertIsDisplayed()
                .assertIsEnabled()
                .performClick()

            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<ConnectionRoute>() == true)
            onNodeWithTag(CONNECTION_SCREEN_TAG).assertIsDisplayed()

            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()

            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<ManualServerRoute>() == true)
        }
    }
}
