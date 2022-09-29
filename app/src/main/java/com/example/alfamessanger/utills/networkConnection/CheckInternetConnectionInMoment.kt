package com.example.alfamessanger.utills.networkConnection

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject

class CheckInternetConnectionInMoment @Inject constructor(private val context: Context){
    private var connectivity : ConnectivityManager? = null
    private var info : NetworkInfo? = null

    fun check() : Boolean{
        var checkResult = false
        connectivity = context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(connectivity != null){
            info = connectivity!!.activeNetworkInfo
            if(info != null){
                checkResult = info!!.state == NetworkInfo.State.CONNECTED
            }
        }
        return checkResult
    }
}