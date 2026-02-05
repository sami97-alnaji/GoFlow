package com.goflow.app.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.core.content.getSystemService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.goflow.app.common.data.network.NetworkHelper
import com.goflow.app.common.data.network.NetworkHelperImpl
import com.goflow.app.common.data.network.NetworkStatusMonitor
import com.goflow.app.common.data.network.NetworkStatusMonitorImpl
import com.goflow.app.common.data.network.WifiHelper
import com.goflow.app.common.data.network.WifiHelperImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    companion object {
        @Provides
        @Singleton
        fun provideConnectivityManager(@ApplicationContext appContext: Context) =
            checkNotNull(appContext.getSystemService<ConnectivityManager>()) {
                "ConnectivityManager is not available on this device"
            }

        @Provides
        @Singleton
        fun provideWifiManager(@ApplicationContext appContext: Context): WifiManager? =
            appContext.getSystemService<WifiManager>()
    }

    @Binds
    @Singleton
    abstract fun bindWifiHelper(wifiHelper: WifiHelperImpl): WifiHelper

    @Binds
    @Singleton
    abstract fun bindNetworkHelper(networkHelper: NetworkHelperImpl): NetworkHelper

    @Binds
    @Singleton
    abstract fun bindNetworkStatusMonitor(networkStatusMonitor: NetworkStatusMonitorImpl): NetworkStatusMonitor
}
