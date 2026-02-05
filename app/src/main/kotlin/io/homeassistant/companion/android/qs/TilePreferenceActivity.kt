package com.goflow.app.qs

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import com.goflow.app.BaseActivity
import com.goflow.app.common.data.servers.ServerManager
import com.goflow.app.database.qs.TileDao
import com.goflow.app.database.qs.isSetup
import com.goflow.app.launch.LaunchActivity
import com.goflow.app.settings.SettingsActivity
import com.goflow.app.settings.qs.ManageTilesViewModel
import com.goflow.app.webview.WebViewActivity
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class TilePreferenceActivity : BaseActivity() {

    @Inject
    lateinit var serverManager: ServerManager

    @Inject
    lateinit var tileDao: TileDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var tileId = "-1"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.extras?.let { extras ->
                BundleCompat.getParcelable(
                    extras,
                    Intent.EXTRA_COMPONENT_NAME,
                    ComponentName::class.java,
                )?.let { component ->
                    try {
                        val tileClass = Class.forName(component.className)
                        val tileMap = ManageTilesViewModel.idToTileService
                        tileMap.filter { it.value == tileClass }.entries.firstOrNull()?.key?.let {
                            Timber.d("Tile ID for long press action: $it")
                            tileId = it
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Couldn't get tile ID for component $component")
                    }
                }
            }
        }

        lifecycleScope.launch {
            val tileData = tileDao.get(tileId)

            val intent = if (!serverManager.isRegistered()) {
                Intent(this@TilePreferenceActivity, LaunchActivity::class.java)
            } else if (tileData?.isSetup == true) {
                WebViewActivity.newInstance(
                    this@TilePreferenceActivity,
                    path = "entityId:${tileData.entityId}",
                    serverId = tileData.serverId,
                )
            } else {
                SettingsActivity.newInstance(this@TilePreferenceActivity, SettingsActivity.Deeplink.QSTile(tileId))
            }

            withContext(Dispatchers.Main) {
                startActivity(intent)
                finish()
                @Suppress("DEPRECATION")
                overridePendingTransition(0, 0) // Disable activity start/stop animation
            }
        }
    }
}
