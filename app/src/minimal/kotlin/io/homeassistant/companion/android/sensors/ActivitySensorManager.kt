package com.goflow.app.sensors

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.goflow.app.common.R as commonR
import com.goflow.app.common.sensors.SensorManager

class ActivitySensorManager :
    BroadcastReceiver(),
    SensorManager {

    override fun onReceive(context: Context, intent: Intent) {
        // Noop
    }
    override val name: Int
        get() = commonR.string.sensor_name_activity

    override suspend fun getAvailableSensors(context: Context): List<SensorManager.BasicSensor> {
        return listOf()
    }

    override fun requiredPermissions(context: Context, sensorId: String): Array<String> {
        // Noop
        return emptyArray()
    }

    override suspend fun requestSensorUpdate(context: Context) {
        // Noop
    }
}
