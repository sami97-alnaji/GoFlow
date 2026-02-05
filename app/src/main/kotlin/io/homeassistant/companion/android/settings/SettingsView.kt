package com.goflow.app.settings

import android.content.pm.PackageManager

interface SettingsView {
    fun onAddServerResult(success: Boolean, serverId: Int?)
    fun getPackageManager(): PackageManager?
}
