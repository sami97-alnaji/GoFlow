package com.goflow.app.common.bluetooth

data class BluetoothDevice(val address: String, val name: String, val paired: Boolean, val connected: Boolean)
