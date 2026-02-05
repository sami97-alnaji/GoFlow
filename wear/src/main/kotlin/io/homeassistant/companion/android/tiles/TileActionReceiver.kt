package com.goflow.app.tiles

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import com.goflow.app.common.data.integration.onEntityPressedWithoutState
import com.goflow.app.common.data.prefs.WearPrefsRepository
import com.goflow.app.common.data.servers.ServerManager
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@AndroidEntryPoint
class TileActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var serverManager: ServerManager

    @Inject
    lateinit var wearPrefsRepository: WearPrefsRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val entityId: String? = intent?.getStringExtra("entity_id")

        if (entityId != null) {
            runBlocking {
                if (wearPrefsRepository.getWearHapticFeedback() && context != null) hapticClick(context)

                try {
                    onEntityPressedWithoutState(
                        entityId = entityId,
                        integrationRepository = serverManager.integrationRepository(),
                    )
                } catch (e: Exception) {
                    Timber.e(e, "Cannot call tile service")
                }
            }
        }
    }
}
