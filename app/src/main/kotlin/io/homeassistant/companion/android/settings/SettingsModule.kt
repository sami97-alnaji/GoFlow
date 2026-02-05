package com.goflow.app.settings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import com.goflow.app.settings.developer.DeveloperSettingsPresenter
import com.goflow.app.settings.developer.DeveloperSettingsPresenterImpl
import com.goflow.app.settings.server.ServerSettingsPresenter
import com.goflow.app.settings.server.ServerSettingsPresenterImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class SettingsModule {

    @Binds
    abstract fun developerSettingsPresenter(
        developerSettingsPresenterImpl: DeveloperSettingsPresenterImpl,
    ): DeveloperSettingsPresenter

    @Binds
    abstract fun serverSettingsPresenter(
        serverSettingsPresenterImpl: ServerSettingsPresenterImpl,
    ): ServerSettingsPresenter

    @Binds
    abstract fun settingsPresenter(settingsPresenterImpl: SettingsPresenterImpl): SettingsPresenter
}
