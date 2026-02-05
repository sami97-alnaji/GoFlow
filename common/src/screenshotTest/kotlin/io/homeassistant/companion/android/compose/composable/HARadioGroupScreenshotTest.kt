package com.goflow.app.compose.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.composable.HARadioGroup
import com.goflow.app.common.compose.composable.RadioOption
import com.goflow.app.common.compose.theme.HAThemeForPreview

class HARadioGroupScreenshotTest {

    @PreviewLightDark
    @PreviewTest
    @Composable
    fun `HARadioGroup no selection`() {
        HAThemeForPreview {
            HARadioGroup(
                options = listOf(
                    RadioOption(
                        "key1",
                        "Title",
                        "SubTitle",
                    ),
                    RadioOption(
                        "key2",
                        "Title2",
                    ),
                    RadioOption(
                        "key3",
                        "Title2",
                        enabled = false,
                    ),
                    RadioOption(
                        "key3",
                        "Very long text, to verifiy that nothing is broken when it is displayed within the bounds.",
                        enabled = false,
                    ),
                ),
                onSelect = {},
                selectedOption = null,
            )
        }
    }

    @PreviewLightDark
    @PreviewTest
    @Composable
    fun `HARadioGroup with selection`() {
        HAThemeForPreview {
            val selectedOption = RadioOption(
                "key3",
                "Very long text, to verifiy that nothing is broken when it is displayed within the bounds.",
                enabled = true,
            )
            HARadioGroup(
                options = listOf(
                    selectedOption,
                    RadioOption(
                        "key1",
                        "Title",
                        "SubTitle",
                    ),
                    RadioOption(
                        "key2",
                        "Title2",
                    ),
                    RadioOption(
                        "key4",
                        "Title3",
                        enabled = false,
                    ),
                ),
                onSelect = {},
                selectedOption = selectedOption,
            )
        }
    }
}
