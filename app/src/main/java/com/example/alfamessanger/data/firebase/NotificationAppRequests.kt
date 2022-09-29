package com.example.alfamessanger.data.firebase

import android.util.Log
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.NotificationModel
import com.example.alfamessanger.domain.models.NotificationsMainModel
import com.example.alfamessanger.domain.repository.NotificationAppRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import com.google.firebase.database.ktx.getValue

class NotificationAppRequests : NotificationAppRepository {

    private lateinit var notification : NotificationsMainModel

    override fun sendNotificationApp(
        friendId: String,
        notificationsMainModel: Map<String, Boolean>,
        notificationModel: NotificationModel,
        onSuccess: () -> Unit
    ) {
        DATABASE_REFERENCE.child(NODE_NOTIFICATIONS).child(friendId).updateChildren(notificationsMainModel).addOnSuccessListener {
            DATABASE_REFERENCE.child(NODE_NOTIFICATIONS).child(friendId).child(
                CHILD_NOTIFICATION_LIST).child(notificationModel.id).setValue(notificationModel).addOnSuccessListener {
                    onSuccess()
            }.addOnFailureListener {
                Log.e(TAG_CREATE_NOTIFICATION, it.message.toString())
            }
        }
    }

    override fun sendNotificationPrivateAccountApp(
        friendId: String,
        notificationsMainModel: Map<String, Boolean>,
        notificationModel: NotificationModel,
        onSuccess: () -> Unit
    ) {
        DATABASE_REFERENCE.child(NODE_NOTIFICATIONS).child(friendId).updateChildren(notificationsMainModel).addOnSuccessListener {
            DATABASE_REFERENCE.child(NODE_NOTIFICATIONS).child(friendId).child(
                CHILD_NOTIFICATION_LIST).child(notificationModel.id).setValue(notificationModel).addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                Log.e(TAG_CREATE_NOTIFICATION, it.message.toString())
            }
        }
    }

    override fun getNotificationsList(onSuccess: (notificationMainModel: NotificationsMainModel) -> Unit) {
        DATABASE_REFERENCE.child(NODE_NOTIFICATIONS).child(UID).addListenerForSingleValueEvent(AppValueEventListener{
            if(it.exists()){
                notification = it.getValue(NotificationsMainModel :: class.java) ?: NotificationsMainModel()
                onSuccess(notification)
            }
        })
    }


}