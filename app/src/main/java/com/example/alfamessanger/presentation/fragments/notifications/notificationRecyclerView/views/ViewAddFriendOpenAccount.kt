package com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.views

import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView

class ViewAddFriendOpenAccount(
    override val id: String,
    override val iconUrl: String,
    override val userFrom: String = "",
    override val channelId: String = "",
    override val timeStamp: Any,
    override val userIdFrom : String

) : NotificationView {
    override fun getTypeView(): Int {
        return NotificationView.NOTIFICATION_ADD_FRIEND_OPEN_ACCOUNT
    }

    override fun equals(other: Any?): Boolean {
        return (other as NotificationView).id == id
    }
}