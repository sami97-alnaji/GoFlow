package com.goflow.app.onboarding.welcome.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import com.goflow.app.common.R as commonR
import com.goflow.app.onboarding.BaseOnboardingNavigationTest
import com.goflow.app.onboarding.URL_GETTING_STARTED_DOCUMENTATION
import com.goflow.app.testing.unit.stringResource
import com.goflow.app.util.compose.navigateToUri
import io.mockk.coVerify
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Navigation tests for the Welcome screen in the onboarding flow.
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
internal class WelcomeNavigationTest : BaseOnboardingNavigationTest() {

    @Test
    fun `Given no action when starting the app then show Welcome`() {
        testNavigation {
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<WelcomeRoute>() == true)
            onNodeWithText(stringResource(commonR.string.welcome_learn_more))
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()
            coVerify { any<NavController>().navigateToUri(URL_GETTING_STARTED_DOCUMENTATION, any()) }
        }
    }
}
