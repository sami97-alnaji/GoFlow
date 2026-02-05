package com.goflow.app.vehicle

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import com.goflow.app.common.R as commonR
import com.goflow.app.database.server.Server
import com.goflow.app.util.vehicle.getHeaderBuilder
import kotlinx.coroutines.flow.StateFlow

class ChangeServerScreen(
    carContext: CarContext,
    private val servers: List<Server>,
    private val serverId: StateFlow<Int>,
) : Screen(carContext) {
    override fun onGetTemplate(): Template {
        val listBuilder = ItemList.Builder()
        servers.forEach { server ->
            listBuilder.addItem(
                Row.Builder()
                    .setTitle(server.friendlyName)
                    .setEnabled(server.id != serverId.value)
                    .setOnClickListener {
                        setResult(server.id)
                        finish()
                    }
                    .build(),
            )
        }

        return ListTemplate.Builder().apply {
            setHeader(carContext.getHeaderBuilder(commonR.string.aa_change_server).build())
            setLoading(false)
            setSingleList(listBuilder.build())
        }.build()
    }
}
