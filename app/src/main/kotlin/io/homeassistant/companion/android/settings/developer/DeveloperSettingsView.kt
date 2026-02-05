package com.goflow.app.settings.developer

import android.content.IntentSender

interface DeveloperSettingsView {
    fun onThreadPermissionRequest(intent: IntentSender, serverId: Int, isDeviceOnly: Boolean)
    fun onThreadDebugResult(result: String, success: Boolean?)
    fun onWebViewClearCacheResult(success: Boolean)
}
