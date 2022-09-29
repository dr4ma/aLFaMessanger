package com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.viewHolders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.views.NotificationView
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.downloadAndSetImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.messages_file_layout.view.*
import kotlinx.android.synthetic.main.notification_friend_add_open_account.view.*

class HolderAddFriendOpenAccount(view: View) : RecyclerView.ViewHolder(view),
    NotificationHolder {

    private val photoUser: CircleImageView = view.photo_user_notification_open_add_friend
    private val nameUser: TextView = view.name_user_notification

    override fun drawNotification(view: NotificationView) {
        if (view.iconUrl != "") {
            photoUser.downloadAndSetImage(true, view.iconUrl)
        } else {
            photoUser.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
        nameUser.text = view.userFrom
    }

    override fun onAttach(view: NotificationView) {
    }

    override fun onDetach() {
    }
}