package com.goflow.app.onboarding.manual

import androidx.annotation.StringRes
import com.goflow.app.database.server.TemporaryServer

interface ManualSetupView {
    fun startIntegration(temporaryServer: TemporaryServer)

    fun showLoading()

    fun showContinueOnPhone()

    fun showError(@StringRes message: Int)
}
