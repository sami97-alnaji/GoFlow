package com.goflow.app.settings

import android.content.Context
import androidx.preference.PreferenceDataStore
import com.goflow.app.common.data.integration.impl.entities.RateLimitResponse
import com.goflow.app.database.server.Server
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SettingsPresenter {
    companion object {
        const val SUGGESTION_ASSISTANT_APP = "assistant_app"
        const val SUGGESTION_NOTIFICATION_PERMISSION = "notification_permission"
    }

    fun init(view: SettingsView)
    fun getPreferenceDataStore(): PreferenceDataStore
    fun onFinish()
    fun updateSuggestions(context: Context)
    fun cancelSuggestion(context: Context, id: String)
    fun getSuggestionFlow(): StateFlow<SettingsHomeSuggestion?>
    suspend fun getServersFlow(): Flow<List<Server>>
    suspend fun getNotificationRateLimits(): RateLimitResponse?
    suspend fun showChangeLog(context: Context)
    suspend fun isChangeLogPopupEnabled(): Boolean
    suspend fun setChangeLogPopupEnabled(enabled: Boolean)
}
