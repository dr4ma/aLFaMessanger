package com.example.alfamessanger.data.retrofit

import android.util.Log
import com.example.alfamessanger.domain.models.PushNotification
import com.example.alfamessanger.domain.repository.NotificationsRepository
import com.example.alfamessanger.utills.TAG_NOTIFICATION
import java.lang.Exception

class NotificationsRequests : NotificationsRepository {

    override suspend fun sendNotification(notification: PushNotification) {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (!response.isSuccessful) {
                Log.e(TAG_NOTIFICATION, response.errorBody().toString())
            }
        } catch (e: Exception) {
            Log.e(TAG_NOTIFICATION, e.message.toString())
        }
    }
}