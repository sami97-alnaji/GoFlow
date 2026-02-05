package com.goflow.app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.goflow.app.common.util.MessagingToken
import com.goflow.app.common.util.MessagingTokenProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MinimalApplicationModule {
    @Provides
    @Singleton
    fun provideMessagingTokenProvider(): MessagingTokenProvider {
        return MessagingTokenProvider {
            return@MessagingTokenProvider MessagingToken("")
        }
    }
}
