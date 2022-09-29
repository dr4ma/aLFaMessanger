package com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.views

import com.example.alfamessanger.domain.models.NotificationModel
import com.example.alfamessanger.utills.TYPE_ADD_FRIEND_CLOSE
import com.example.alfamessanger.utills.TYPE_ADD_FRIEND_OPEN

class AppViewNotificationFactory {

    companion object{
        fun getView(notificationModel: NotificationModel) : NotificationView{
            return when(notificationModel.type){
                TYPE_ADD_FRIEND_OPEN -> ViewAddFriendOpenAccount(
                    id = notificationModel.id,
                    iconUrl = notificationModel.iconUrl,
                    userFrom = notificationModel.userFrom,
                    channelId = notificationModel.channelId,
                    timeStamp = notificationModel.timeStamp,
                    userIdFrom = notificationModel.userIdFrom
                )
                else -> ViewAddFriendCloseAccount(
                    id = notificationModel.id,
                    iconUrl = notificationModel.iconUrl,
                    userFrom = notificationModel.userFrom,
                    channelId = notificationModel.channelId,
                    timeStamp = notificationModel.timeStamp,
                    userIdFrom = notificationModel.userIdFrom
                )
            }
        }
    }
}