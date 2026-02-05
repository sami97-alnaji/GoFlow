package com.goflow.app.onboarding.welcome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.goflow.app.onboarding.welcome.WelcomeScreen
import kotlinx.serialization.Serializable

@Serializable
internal data object WelcomeRoute

internal fun NavController.navigateToWelcome(navOptions: NavOptions? = null) {
    navigate(route = WelcomeRoute, navOptions)
}

internal fun NavGraphBuilder.welcomeScreen(onConnectClick: () -> Unit, onLearnMoreClick: suspend () -> Unit) {
    composable<WelcomeRoute> {
        WelcomeScreen(onConnectClick = onConnectClick, onLearnMoreClick = onLearnMoreClick)
    }
}
