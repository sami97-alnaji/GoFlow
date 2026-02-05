package com.goflow.app.onboarding.integration

import com.goflow.app.database.server.TemporaryServer

interface MobileAppIntegrationPresenter {
    fun onRegistrationAttempt(temporaryServer: TemporaryServer, deviceName: String)
    fun onFinish()
}
