package com.example.alfamessanger.di

import android.content.Context
import com.example.alfamessanger.utills.networkConnection.CheckInternetConnectionInMoment
import com.example.alfamessanger.utills.networkConnection.ConnectivityObserver
import com.example.alfamessanger.utills.networkConnection.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class ActivityModule {

    @Provides
    fun provideNetworkConnection(@ApplicationContext context: Context) : ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

    @Provides
    fun provideNetworkConnectionInMoment(@ApplicationContext context: Context): CheckInternetConnectionInMoment{
        return CheckInternetConnectionInMoment(context)
    }
}