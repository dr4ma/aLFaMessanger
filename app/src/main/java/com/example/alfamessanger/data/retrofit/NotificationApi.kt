package com.example.alfamessanger.data.retrofit

import com.example.alfamessanger.domain.models.PushNotification
import com.example.alfamessanger.utills.CONTENT_TYPE
import com.example.alfamessanger.utills.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key = $SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}