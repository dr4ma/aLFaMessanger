package com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.views.NotificationView
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders.HolderImageMessage

class AppHolderNotificationFactory {
    companion object{
        fun getHolder(parent : ViewGroup, viewType : Int) : RecyclerView.ViewHolder{
            return when(viewType){
                NotificationView.NOTIFICATION_ADD_FRIEND_OPEN_ACCOUNT -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.notification_friend_add_open_account, parent, false)
                    HolderAddFriendOpenAccount(view)
                }
                else -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.notification_add_friend_close_account, parent, false)
                    HolderAddFriendCloseAccount(view)
                }
            }
        }
    }
}