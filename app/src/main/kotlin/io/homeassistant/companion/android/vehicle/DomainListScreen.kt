package com.goflow.app.vehicle

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.car.app.CarContext
import androidx.car.app.model.GridTemplate
import androidx.car.app.model.Template
import androidx.lifecycle.lifecycleScope
import com.goflow.app.BuildConfig
import com.goflow.app.common.R
import com.goflow.app.common.data.integration.Entity
import com.goflow.app.common.data.prefs.PrefsRepository
import com.goflow.app.common.data.servers.ServerManager
import com.goflow.app.common.data.websocket.impl.entities.EntityRegistryResponse
import com.goflow.app.common.util.isAutomotive
import com.goflow.app.util.vehicle.SUPPORTED_DOMAINS
import com.goflow.app.util.vehicle.getDomainList
import com.goflow.app.util.vehicle.getHeaderBuilder
import com.goflow.app.util.vehicle.nativeModeAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class DomainListScreen(
    carContext: CarContext,
    val serverManager: ServerManager,
    private val serverId: StateFlow<Int>,
    private val allEntities: Flow<Map<String, Entity>>,
    private val prefsRepository: PrefsRepository,
    private val entityRegistry: List<EntityRegistryResponse>?,
) : BaseVehicleScreen(carContext) {

    private val domains = mutableSetOf<String>()
    private var domainsAdded = false

    override fun onDrivingOptimizedChanged(newState: Boolean) {
        invalidate()
    }

    init {
        lifecycleScope.launch {
            allEntities.collect { entities ->
                val newDomains = entities.values
                    .map { it.domain }
                    .distinct()
                    .filter { it in SUPPORTED_DOMAINS }
                    .toSet()
                val invalidate = newDomains.size != domains.size || newDomains != domains || !domainsAdded
                domains.clear()
                domains.addAll(newDomains)
                domainsAdded = true
                if (invalidate) invalidate()
            }
        }
    }

    override fun onGetTemplate(): Template {
        val isAutomotive = carContext.isAutomotive()
        val domainList = getDomainList(
            domains,
            carContext,
            screenManager,
            serverManager,
            serverId,
            prefsRepository,
            allEntities,
            entityRegistry,
            lifecycleScope,
        )

        return GridTemplate.Builder().apply {
            val headerBuilder = carContext.getHeaderBuilder(R.string.all_entities)
            if (isAutomotive && !isDrivingOptimized && BuildConfig.FLAVOR != "full") {
                headerBuilder.addEndHeaderAction(nativeModeAction(carContext))
            }
            setHeader(headerBuilder.build())
            val domainBuild = domainList.build()
            if (!domainsAdded) {
                setLoading(true)
            } else {
                setLoading(false)
                setSingleList(domainBuild)
            }
        }.build()
    }
}
