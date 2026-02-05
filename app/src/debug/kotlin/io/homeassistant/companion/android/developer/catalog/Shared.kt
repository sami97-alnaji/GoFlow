package com.goflow.app.developer.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.goflow.app.common.compose.theme.HADimens
import com.goflow.app.common.compose.theme.HATextStyle

@Composable
fun CatalogRow(content: @Composable () -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(HADimens.SPACE4),
        verticalArrangement = Arrangement.spacedBy(HADimens.SPACE4),
    ) {
        content()
    }
}

fun LazyListScope.catalogSection(title: String, content: @Composable () -> Unit) {
    item {
        Text(text = title, modifier = Modifier.padding(top = HADimens.SPACE4), style = HATextStyle.Body)
    }
    item {
        content()
    }
}
