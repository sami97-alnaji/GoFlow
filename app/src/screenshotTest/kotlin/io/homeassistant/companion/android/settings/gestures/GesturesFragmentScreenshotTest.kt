package com.goflow.app.settings.gestures

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.util.GestureAction
import com.goflow.app.common.util.HAGesture
import com.goflow.app.settings.gestures.views.GestureActionsView
import com.goflow.app.settings.gestures.views.GesturesListView
import com.goflow.app.util.compose.HomeAssistantAppTheme

class GesturesFragmentScreenshotTest {

    @PreviewTest
    @Preview
    @Composable
    fun `Gestures list with no action for each gesture`() {
        HomeAssistantAppTheme {
            GesturesListView(
                gestureActions = HAGesture.entries.associateWith { GestureAction.NONE },
                onGestureClicked = { _ -> },
            )
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun `Gesture actions with search entities selected`() {
        HomeAssistantAppTheme {
            GestureActionsView(
                selectedAction = GestureAction.QUICKBAR_DEFAULT,
                onActionClicked = {},
            )
        }
    }
}
