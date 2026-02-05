package com.goflow.app.settings.vehicle.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goflow.app.common.R as commonR
import com.goflow.app.common.compose.theme.HATheme
import com.goflow.app.common.data.integration.Entity
import com.goflow.app.common.data.integration.friendlyName
import com.goflow.app.common.data.prefs.AutoFavorite
import com.goflow.app.database.server.Server
import com.goflow.app.settings.vehicle.ManageAndroidAutoViewModel
import com.goflow.app.util.compose.FavoriteEntityRow
import com.goflow.app.util.compose.ServerExposedDropdownMenu
import com.goflow.app.util.compose.entity.EntityPicker
import com.goflow.app.util.plus
import com.goflow.app.util.safeBottomPaddingValues
import com.goflow.app.util.vehicle.isVehicleDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AndroidAutoFavoritesSettings(
    androidAutoViewModel: ManageAndroidAutoViewModel,
    serversList: List<Server>,
    defaultServer: Int,
) {
    val lazyListState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(lazyListState) { from, to ->
        androidAutoViewModel.onMove(from, to)
        androidAutoViewModel.saveFavorites()
    }

    var selectedServer by remember(defaultServer) { mutableIntStateOf(defaultServer) }

    val favoriteEntities = androidAutoViewModel.favoritesList.toList()
    var validEntities by remember { mutableStateOf<List<Entity>>(emptyList()) }
    LaunchedEffect(favoriteEntities.size, androidAutoViewModel.sortedEntities.size, selectedServer) {
        validEntities = withContext(Dispatchers.IO) {
            androidAutoViewModel.sortedEntities
                .filter {
                    !favoriteEntities.contains(AutoFavorite(selectedServer, it.entityId)) &&
                        isVehicleDomain(it)
                }
                .toList()
        }
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(vertical = 16.dp) + safeBottomPaddingValues(applyHorizontal = false),
    ) {
        item {
            Text(
                text = stringResource(commonR.string.aa_set_favorites),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
            )
        }

        if (serversList.size > 1) {
            item {
                ServerExposedDropdownMenu(
                    servers = serversList,
                    current = selectedServer,
                    onSelected = {
                        androidAutoViewModel.loadEntities(it)
                        selectedServer = it
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp),
                )
            }
        }
        item {
            // TODO use new theme for Material3 components https://github.com/home-assistant/android/issues/6302
            HATheme {
                EntityPicker(
                    entities = validEntities,
                    selectedEntityId = null,
                    onEntityCleared = { /* Nothing */ },
                    onEntitySelectedId = {
                        androidAutoViewModel.onEntitySelected(true, it, selectedServer)
                    },
                    addButtonText = stringResource(commonR.string.add_favorite),
                    entityRegistry = androidAutoViewModel.entityRegistry,
                    deviceRegistry = androidAutoViewModel.deviceRegistry,
                    areaRegistry = androidAutoViewModel.areaRegistry,
                    modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
                )
            }
        }
        if (favoriteEntities.isNotEmpty() && androidAutoViewModel.sortedEntities.isNotEmpty()) {
            items(favoriteEntities.size, { favoriteEntities[it] }) { index ->
                val favoriteEntity = favoriteEntities[index]
                androidAutoViewModel.sortedEntities.firstOrNull {
                    it.entityId == favoriteEntity.entityId &&
                        favoriteEntity.serverId == selectedServer
                }?.let {
                    ReorderableItem(
                        state = reorderState,
                        key = favoriteEntities[index],
                    ) { isDragging ->
                        FavoriteEntityRow(
                            entityName = it.friendlyName,
                            entityId = it.entityId,
                            onClick = {
                                androidAutoViewModel.onEntitySelected(
                                    false,
                                    it.entityId,
                                    selectedServer,
                                )
                            },
                            checked = true,
                            draggable = true,
                            isDragging = isDragging,
                        )
                    }
                }
            }
        }
    }
}
