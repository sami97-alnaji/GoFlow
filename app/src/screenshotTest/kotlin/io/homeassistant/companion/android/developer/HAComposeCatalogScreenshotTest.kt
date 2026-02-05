package com.goflow.app.developer

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import com.goflow.app.common.compose.composable.ButtonVariant
import com.goflow.app.common.compose.theme.HAThemeForPreview
import com.goflow.app.developer.catalog.catalogButtonsAndIndicatorsSection
import com.goflow.app.developer.catalog.catalogTextAndBannersSection
import com.goflow.app.developer.catalog.catalogUserInputSection

class HAComposeCatalogScreenshotTest {

    @CatalogScreenPreview
    @PreviewTest
    @Composable
    fun HAButtonsAndIndicatorsScreen() {
        HAThemeForPreview {
            LazyColumn {
                catalogButtonsAndIndicatorsSection(ButtonVariant.PRIMARY)
            }
        }
    }

    @CatalogScreenPreview
    @PreviewTest
    @Composable
    fun HAUserInputScreen() {
        HAThemeForPreview {
            LazyColumn {
                catalogUserInputSection()
            }
        }
    }

    @CatalogScreenPreview
    @PreviewTest
    @Composable
    fun HATextAndBannersScreen() {
        HAThemeForPreview {
            LazyColumn {
                catalogTextAndBannersSection()
            }
        }
    }
}
