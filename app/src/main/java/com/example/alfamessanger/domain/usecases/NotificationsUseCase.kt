package com.example.alfamessanger.domain.usecases

import com.example.alfamessanger.domain.models.*
import com.example.alfamessanger.domain.repository.NotificationsRepository
import com.example.alfamessanger.utills.TOPIC
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class NotificationsUseCase @Inject constructor(private val notificationsRepository: NotificationsRepository) {

    suspend fun sendNotification(userModel : UserModel, tittle : String, message : String){
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        if (userModel.tokenNotification.isNotEmpty()) {
            PushNotification(
                NotificationData(tittle, message),
                userModel.tokenNotification
            ).also { push ->
                notificationsRepository.sendNotification(push)
            }
        }
    }
}