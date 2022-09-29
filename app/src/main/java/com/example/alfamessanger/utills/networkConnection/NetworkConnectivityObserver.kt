package com.example.alfamessanger.utills.networkConnection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.enums.StatusConnection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkConnectivityObserver(private val context: Context) : ConnectivityObserver {

    override fun observe(): Flow<StatusConnection> {
         val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        send(StatusConnection.AVAILABLE)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(StatusConnection.LOSING)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(StatusConnection.LOST)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(StatusConnection.UNAVAILABLE)
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose{
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}