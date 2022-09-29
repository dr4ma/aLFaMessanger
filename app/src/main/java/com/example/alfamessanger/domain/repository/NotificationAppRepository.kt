package com.example.alfamessanger.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.NotificationModel
import com.example.alfamessanger.domain.models.NotificationsMainModel
import com.example.alfamessanger.domain.models.UserModel

interface NotificationAppRepository {

    fun sendNotificationApp(
        friendId: String,
        notificationsMainModel: Map<String, Boolean>,
        notificationModel: NotificationModel,
        onSuccess: () -> Unit
    )

    fun getNotificationsList(onSuccess: (notificationMainModel : NotificationsMainModel) -> Unit)
}