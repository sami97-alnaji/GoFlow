package com.goflow.app.sensors

import com.goflow.app.BuildConfig
import com.goflow.app.common.sensors.AppSensorManagerBase

class AppSensorManager : AppSensorManagerBase() {

    override fun getCurrentVersion(): String = BuildConfig.VERSION_NAME
}
