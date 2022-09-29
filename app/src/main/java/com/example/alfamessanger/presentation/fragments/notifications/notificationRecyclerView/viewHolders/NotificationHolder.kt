package com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.viewHolders

import com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.views.NotificationView

interface NotificationHolder {

    fun drawNotification(view: NotificationView)
    fun onAttach(view : NotificationView)
    fun onDetach()
}