package com.goflow.app.onboarding

import androidx.annotation.StringRes
import com.goflow.app.database.server.TemporaryServer

interface OnboardingView {
    fun startIntegration(temporaryServer: TemporaryServer)

    fun onInstanceFound(instance: HomeAssistantInstance)
    fun onInstanceLost(instance: HomeAssistantInstance)

    fun showLoading()

    fun showContinueOnPhone()

    fun showError(@StringRes message: Int? = null)
}
