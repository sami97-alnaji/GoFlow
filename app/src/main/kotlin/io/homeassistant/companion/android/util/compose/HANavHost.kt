package com.goflow.app.util.compose

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.goflow.app.automotive.navigation.carAppActivity
import com.goflow.app.automotive.navigation.navigateToCarAppActivity
import com.goflow.app.common.util.isAutomotive
import com.goflow.app.frontend.navigation.frontendScreen
import com.goflow.app.frontend.navigation.navigateToFrontend
import com.goflow.app.launch.HAStartDestinationRoute
import com.goflow.app.loading.LoadingScreen
import com.goflow.app.loading.navigation.LoadingRoute
import com.goflow.app.loading.navigation.loadingScreen
import com.goflow.app.onboarding.OnboardingRoute
import com.goflow.app.onboarding.WearOnboardApp
import com.goflow.app.onboarding.WearOnboardingRoute
import com.goflow.app.onboarding.onboarding
import com.goflow.app.onboarding.wearOnboarding

/**
 * Navigation host for the main application.
 *
 * This composable function sets up the navigation graph for the whole app.
 * The [NavHost] start destination is always [LoadingRoute] until something triggers a navigation
 * to a different destination.
 *
 * @param navController The [NavHostController] for managing navigation.
 * @param startDestination The initial destination of the navigation graph. If it is null [LoadingScreen]
 *                         is displayed.
 * @param onShowSnackbar A suspending function to display a snackbar.
 *                       It takes a [message] and an optional [action] label.
 *                       Returns `true` if the action was performed (if an action was provided),
 *                       `false` otherwise (e.g., dismissed).
 */
@Composable
internal fun HANavHost(
    navController: NavHostController,
    startDestination: HAStartDestinationRoute?,
    onShowSnackbar: suspend (message: String, action: String?) -> Boolean,
) {
    val activity = LocalActivity.current
    val isAutomotive = activity?.isAutomotive() == true

    startDestination?.let {
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            loadingScreen()
            onboarding(
                navController,
                onShowSnackbar = onShowSnackbar,
                onOnboardingDone = {
                    if (isAutomotive) {
                        navController.navigateToCarAppActivity()
                    } else {
                        navController.navigateToFrontend()
                    }
                },
                urlToOnboard = (startDestination as? OnboardingRoute)?.urlToOnboard,
                hideExistingServers = (startDestination as? OnboardingRoute)?.hideExistingServers == true,
                skipWelcome = (startDestination as? OnboardingRoute)?.skipWelcome == true,
                hasLocationTracking = (startDestination as? OnboardingRoute)?.hasLocationTracking == true,
            )
            if (startDestination is WearOnboardingRoute) {
                wearOnboarding(
                    navController,
                    onOnboardingDone = {
                            deviceName: String,
                            serverUrl: String,
                            authCode: String,
                            certUri: Uri?,
                            certPassword: String?,
                        ->
                        activity?.setResult(
                            Activity.RESULT_OK,
                            WearOnboardApp.Output(
                                url = serverUrl,
                                authCode = authCode,
                                deviceName = deviceName,
                                tlsClientCertificateUri = certUri?.toString(),
                                tlsClientCertificatePassword = certPassword,
                            ).toIntent(),
                        )
                        activity?.finish()
                    },
                    onShowSnackbar = onShowSnackbar,
                    urlToOnboard = startDestination.urlToOnboard,
                    wearNameToOnboard = startDestination.wearName,
                )
            }
            frontendScreen(navController)
            if (isAutomotive) {
                carAppActivity(navController)
            }
        }
    } ?: LoadingScreen()
}
