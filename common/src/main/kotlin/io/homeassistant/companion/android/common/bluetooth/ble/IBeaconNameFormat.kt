package com.goflow.app.common.bluetooth.ble

interface IBeaconNameFormat {
    val uuid: String
    val major: String
    val minor: String
}

val IBeaconNameFormat.name get() = "${uuid}_${major}_$minor"
