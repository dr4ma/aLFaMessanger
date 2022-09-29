package com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.views

interface NotificationView {

    val id: String
    val iconUrl: String
    val userFrom : String
    val channelId : String
    val timeStamp : Any
    val userIdFrom : String


    companion object{
        val NOTIFICATION_ADD_FRIEND_OPEN_ACCOUNT : Int get() = 0
        val NOTIFICATION_ADD_FRIEND_CLOSE_ACCOUNT : Int get() = 1
        val NOTIFICATION_INVITE_IN_HIDE_CHANNEL : Int get() = 2
    }

    fun getTypeView() : Int
}