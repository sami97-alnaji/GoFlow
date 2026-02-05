package com.goflow.app.onboarding.locationsharing

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.goflow.app.R
import com.goflow.app.common.R as commonR
import com.goflow.app.common.compose.composable.HAAccentButton
import com.goflow.app.common.compose.composable.HAPlainButton
import com.goflow.app.common.compose.composable.HATopBar
import com.goflow.app.common.compose.theme.HADimens
import com.goflow.app.common.compose.theme.HATextStyle
import com.goflow.app.common.compose.theme.HAThemeForPreview
import com.goflow.app.common.compose.theme.MaxButtonWidth
import com.goflow.app.common.util.createBatteryOptimizationIntent
import com.goflow.app.util.compose.HAPreviews
import com.goflow.app.util.compose.rememberLocationPermission

private val MaxContentWidth = MaxButtonWidth

@Composable
internal fun LocationSharingScreen(
    onHelpClick: suspend () -> Unit,
    onGoToNextScreen: () -> Unit,
    viewModel: LocationSharingViewModel,
    modifier: Modifier = Modifier,
) {
    LocationSharingScreen(
        onHelpClick = onHelpClick,
        onGoToNextScreen = onGoToNextScreen,
        onLocationSharingResponse = viewModel::setupLocationSensor,
        modifier = modifier,
    )
}

@Composable
internal fun LocationSharingScreen(
    onHelpClick: suspend () -> Unit,
    onGoToNextScreen: () -> Unit,
    onLocationSharingResponse: (enabled: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { HATopBar(onHelpClick = onHelpClick) },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { contentPadding ->
        LocationSharingContent(
            onGoToNextScreen = onGoToNextScreen,
            onLocationSharingResponse = onLocationSharingResponse,
            modifier = Modifier.padding(contentPadding),
        )
    }
}

@Composable
private fun LocationSharingContent(
    onGoToNextScreen: () -> Unit,
    onLocationSharingResponse: (enabled: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = HADimens.SPACE4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HADimens.SPACE6),
    ) {
        Image(
            modifier = Modifier.padding(top = HADimens.SPACE6),
            // Use painterResource instead of vector resource for API < 24 since it has gradients
            painter = painterResource(R.drawable.ic_location_tracking),
            contentDescription = null,
        )

        Text(
            text = stringResource(commonR.string.location_sharing_title),
            style = HATextStyle.Headline,
            modifier = Modifier.widthIn(max = MaxContentWidth),
        )

        Text(
            text = stringResource(commonR.string.location_sharing_content),
            style = HATextStyle.Body,
            modifier = Modifier.widthIn(max = MaxContentWidth),
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomButtons(
            onGoToNextScreen = onGoToNextScreen,
            onLocationSharingResponse = onLocationSharingResponse,
        )
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun BottomButtons(onGoToNextScreen: () -> Unit, onLocationSharingResponse: (enabled: Boolean) -> Unit) {
    val context = LocalContext.current
    val batteryOptimizationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { onGoToNextScreen() },
    )
    val permissions = rememberLocationPermission(
        onPermissionResult = {
            // We ignore the result and proceed even if the user rejected the permission
            val intent = context.createBatteryOptimizationIntent()
            if (intent != null) {
                batteryOptimizationLauncher.launch(intent)
            } else {
                onGoToNextScreen()
            }
        },
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HADimens.SPACE4),
    ) {
        HAAccentButton(
            text = stringResource(commonR.string.location_sharing_share),
            onClick = {
                onLocationSharingResponse(true)
                permissions.launchMultiplePermissionRequest()
            },
            modifier = Modifier.fillMaxWidth(),
        )

        HAPlainButton(
            text = stringResource(commonR.string.location_sharing_no_share),
            onClick = {
                onLocationSharingResponse(false)
                onGoToNextScreen()
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = HADimens.SPACE6),
        )
    }
}

@HAPreviews
@Composable
private fun LocationSharingScreenPreview() {
    HAThemeForPreview {
        LocationSharingScreen(
            onHelpClick = {},
            onGoToNextScreen = {},
            onLocationSharingResponse = {},
            modifier = Modifier,
        )
    }
}
