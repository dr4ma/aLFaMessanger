package com.example.alfamessanger.utills.networkConnection

import android.content.Context
import com.example.alfamessanger.utills.enums.StatusConnection
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe() : Flow<StatusConnection>
}