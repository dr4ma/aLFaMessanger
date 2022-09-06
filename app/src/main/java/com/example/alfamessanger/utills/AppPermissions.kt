package com.example.alfamessanger.utills

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object AppPermissions {

    @SuppressLint("ObsoleteSdkInt")
    fun checkPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                APP_ACTIVITY_MAIN, permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                APP_ACTIVITY_MAIN,
                arrayOf(permission),
                PERMISSION_REQUEST
            )
            false
        } else {
            true
        }
    }
}