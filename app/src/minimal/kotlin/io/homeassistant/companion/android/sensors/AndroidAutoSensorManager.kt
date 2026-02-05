package com.goflow.app.sensors

import android.content.Context
import com.goflow.app.common.R as commonR
import com.goflow.app.common.sensors.SensorManager

class AndroidAutoSensorManager : SensorManager {

    override val name: Int
        get() = commonR.string.sensor_name_android_auto

    override suspend fun getAvailableSensors(context: Context): List<SensorManager.BasicSensor> {
        return listOf()
    }

    override fun requiredPermissions(context: Context, sensorId: String): Array<String> {
        return emptyArray()
    }

    override suspend fun requestSensorUpdate(context: Context) {
        // Noop
    }
}
