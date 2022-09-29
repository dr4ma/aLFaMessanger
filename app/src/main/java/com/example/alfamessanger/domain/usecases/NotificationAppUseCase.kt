package com.example.alfamessanger.domain.usecases

import android.util.Log
import com.example.alfamessanger.data.firebase.SingleChatRequests
import com.example.alfamessanger.domain.models.NotificationModel
import com.example.alfamessanger.domain.models.NotificationsMainModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.NotificationAppRepository
import com.example.alfamessanger.domain.repository.SingleChatRepository
import com.example.alfamessanger.utills.*
import com.google.firebase.database.ServerValue
import javax.inject.Inject

class NotificationAppUseCase @Inject constructor(private val notificationAppRepository: NotificationAppRepository,
private val singleChatRepository: SingleChatRepository) {

    suspend fun sendNotificationApp(friendModel: UserModel, onSuccess:() -> Unit){
        singleChatRepository.createMessageKey(friendModel.uid.toString()){
            val notificationMainModel = mapOf<String, Boolean>(
                WRITTEN to false
            )
            val notificationModel = NotificationModel(
                id = it,
                type = TYPE_ADD_FRIEND_OPEN,
                iconUrl = friendModel.iconUrl,
                userFrom = USER.nickname,
                timeStamp = ServerValue.TIMESTAMP,
                userIdFrom = USER.uid.toString()
            )
            notificationAppRepository.sendNotificationApp(friendModel.uid.toString(), notificationMainModel, notificationModel){
                onSuccess()
            }
        }
    }

    suspend fun sendNotificationPrivateAccountApp(friendModel: UserModel, onSuccess:() -> Unit){
        singleChatRepository.createMessageKey(friendModel.uid.toString()){
            val notificationMainModel = mapOf<String, Boolean>(
                WRITTEN to false
            )
            val notificationModel = NotificationModel(
                id = it,
                type = TYPE_ADD_FRIEND_CLOSE,
                iconUrl = friendModel.iconUrl,
                userFrom = USER.nickname,
                timeStamp = ServerValue.TIMESTAMP,
                userIdFrom = USER.uid.toString()
            )
            notificationAppRepository.sendNotificationPrivateAccountApp(friendModel.uid.toString(), notificationMainModel, notificationModel){
                onSuccess()
            }
        }
    }

    fun getNotificationsList(onSuccess: (list: MutableList<NotificationModel>) -> Unit){
        notificationAppRepository.getNotificationsList {
            val notificationList = ArrayList(it.notification_list.values) as MutableList<NotificationModel>
            Log.e(TAG, notificationList.size.toString())
            onSuccess(notificationList)
        }
    }
}