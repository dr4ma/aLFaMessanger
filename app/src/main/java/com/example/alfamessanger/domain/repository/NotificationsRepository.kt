package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.PushNotification

interface NotificationsRepository {
    suspend fun sendNotification (notification : PushNotification)
}