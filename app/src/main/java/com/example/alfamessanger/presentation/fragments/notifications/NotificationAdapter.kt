package com.example.alfamessanger.presentation.fragments.notifications

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.viewHolders.AppHolderNotificationFactory
import com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.viewHolders.NotificationHolder
import com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.views.NotificationView

class NotificationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var notificationList = mutableListOf<NotificationView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderNotificationFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NotificationHolder).drawNotification(notificationList[position])
    }

    override fun getItemCount(): Int {
       return notificationList.size
    }

    override fun getItemViewType(position: Int): Int {
        return notificationList[position].getTypeView()
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as NotificationHolder).onAttach(notificationList[holder.adapterPosition])
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as NotificationHolder).onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    fun addToList(item : NotificationView){
        if(!notificationList.contains(item)){
            notificationList.add(item)
            notifyItemInserted(notificationList.size)
        }
    }
}