package com.goflow.app.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SettingsHomeSuggestion(
    val id: String,
    @StringRes val title: Int,
    @StringRes val summary: Int,
    @DrawableRes val icon: Int,
)
