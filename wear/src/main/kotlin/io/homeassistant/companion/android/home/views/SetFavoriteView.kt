package com.goflow.app.home.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material3.SwitchButton
import androidx.wear.compose.material3.Text
import com.mikepenz.iconics.compose.Image
import com.goflow.app.common.R as commonR
import com.goflow.app.common.data.integration.Entity
import com.goflow.app.common.data.integration.getIcon
import com.goflow.app.home.MainViewModel
import com.goflow.app.theme.WearAppTheme
import com.goflow.app.theme.getSwitchButtonColors
import com.goflow.app.theme.wearColorScheme
import com.goflow.app.views.ExpandableListHeader
import com.goflow.app.views.ListHeader
import com.goflow.app.views.ThemeLazyColumn
import com.goflow.app.views.rememberExpandedStates

@Composable
fun SetFavoritesView(
    mainViewModel: MainViewModel,
    favoriteEntityIds: List<String>,
    onFavoriteSelected: (entityId: String, isSelected: Boolean) -> Unit,
) {
    // Remember expanded state of each header
    val expandedStates = rememberExpandedStates(mainViewModel.supportedDomains())

    WearAppTheme {
        ThemeLazyColumn {
            item {
                ListHeader(id = commonR.string.set_favorite)
            }
            for (domain in mainViewModel.entitiesByDomainOrder) {
                val entities = mainViewModel.entitiesByDomain[domain].orEmpty()
                if (entities.isNotEmpty()) {
                    item {
                        ExpandableListHeader(
                            string = mainViewModel.stringForDomain(domain)!!,
                            key = domain,
                            expandedStates = expandedStates,
                        )
                    }
                    if (expandedStates[domain] == true) {
                        items(entities, key = { it.entityId }) { entity ->
                            FavoriteToggleChip(
                                entity = entity,
                                favoriteEntityIds = favoriteEntityIds,
                                onFavoriteSelected = onFavoriteSelected,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteToggleChip(
    entity: Entity,
    favoriteEntityIds: List<String>,
    onFavoriteSelected: (entityId: String, isSelected: Boolean) -> Unit,
) {
    val attributes = entity.attributes as Map<*, *>
    val iconBitmap = entity.getIcon(LocalContext.current)

    val entityId = entity.entityId
    val checked = favoriteEntityIds.contains(entityId)
    SwitchButton(
        checked = checked,
        onCheckedChange = {
            onFavoriteSelected(entityId, it)
        },
        modifier = Modifier
            .fillMaxWidth(),
        icon = {
            Image(
                asset = iconBitmap,
                colorFilter = ColorFilter.tint(wearColorScheme.onSurface),
            )
        },
        label = {
            Text(
                text = attributes["friendly_name"].toString(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        colors = getSwitchButtonColors(),
    )
}
