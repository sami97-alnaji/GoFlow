package com.goflow.app.settings.wear.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.goflow.app.settings.wear.SettingsWearViewModel
import com.goflow.app.util.compose.FavoriteEntityRow
import com.goflow.app.util.compose.entity.EntityPicker
import com.goflow.app.util.plus
import com.goflow.app.util.safeBottomPaddingValues
import com.goflow.app.util.safeBottomWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun LoadWearFavoritesSettings(
    settingsWearViewModel: SettingsWearViewModel,
    onBackClicked: () -> Unit,
    events: SharedFlow<String>,
) {
    val lazyListState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(lazyListState) { from, to ->
        settingsWearViewModel.onMove(from, to)
        settingsWearViewModel.sendHomeFavorites(settingsWearViewModel.favoriteEntityIds.toList())
    }

    val favoriteEntities = settingsWearViewModel.favoriteEntityIds
    var validEntities by remember { mutableStateOf<List<Entity>>(emptyList()) }
    LaunchedEffect(favoriteEntities.size) {
        validEntities = withContext(Dispatchers.IO) {
            settingsWearViewModel.entities
                .filter {
                    !favoriteEntities.contains(it.key) &&
                        it.key.split(".")[0] in settingsWearViewModel.supportedDomains
                }
                .values
                .toList()
        }
    }

    val scaffoldState = rememberScaffoldState()
    LaunchedEffect("snackbar") {
        events.onEach { message ->
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() // in case of rapid-fire events
            scaffoldState.snackbarHostState.showSnackbar(message)
        }.launchIn(this)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState,
                modifier = Modifier.windowInsetsPadding(safeBottomWindowInsets()),
            )
        },
        topBar = {
            SettingsWearTopAppBar(
                title = { Text(stringResource(commonR.string.wear_favorite_entities)) },
                onBackClicked = onBackClicked,
                docsLink = WEAR_DOCS_LINK,
            )
        },
    ) { contentPadding ->
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(vertical = 16.dp) + safeBottomPaddingValues(),
            modifier = Modifier.padding(contentPadding),
        ) {
            item {
                Text(
                    text = stringResource(commonR.string.wear_set_favorites),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item {
                // TODO use new theme for Material3 components https://github.com/home-assistant/android/issues/6300
                HATheme {
                    EntityPicker(
                        entities = validEntities,
                        selectedEntityId = null,
                        onEntityCleared = { /* Nothing */ },
                        onEntitySelectedId = {
                            settingsWearViewModel.onEntitySelected(true, it)
                        },
                        addButtonText = stringResource(commonR.string.add_favorite),
                        modifier = Modifier.padding(all = 16.dp),
                        // In order to have the info about the area/device/zone we need the websocket, it would
                        // requires SettingsWearRepository to be able to use the WebSocket which is a significant
                        // work.
                    )
                }
            }
            items(favoriteEntities.size, { favoriteEntities[it] }) { index ->
                val favoriteEntityID = favoriteEntities[index].replace("[", "").replace("]", "")
                settingsWearViewModel.entities[favoriteEntityID]?.let {
                    ReorderableItem(
                        state = reorderState,
                        key = favoriteEntities[index],
                    ) { isDragging ->
                        FavoriteEntityRow(
                            entityName = it.friendlyName,
                            entityId = favoriteEntityID,
                            onClick = {
                                settingsWearViewModel.onEntitySelected(
                                    false,
                                    favoriteEntities[index],
                                )
                            },
                            checked = favoriteEntities.contains(favoriteEntities[index]),
                            draggable = true,
                            isDragging = isDragging,
                        )
                    }
                }
            }
        }
    }
}
