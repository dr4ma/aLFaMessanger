package com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.viewHolders

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.views.NotificationView
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders.MessageHolder
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.downloadAndSetImage
import com.example.alfamessanger.utills.toTimeDifference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.notification_add_friend_close_account.view.*
import kotlinx.android.synthetic.main.notification_friend_add_open_account.view.*
import kotlinx.android.synthetic.main.notification_friend_add_open_account.view.name_user_notification

class HolderAddFriendCloseAccount(view: View) : RecyclerView.ViewHolder(view),
    NotificationHolder {

    private val photoUser: CircleImageView = view.photo_user_notification_close_add_friend
    private val nameUser: TextView = view.name_user_notification
    private val time: TextView = view.time_close_account
    private val acceptFriend: Button = view.accept_friend
    private val disableFriend: Button = view.disable_friend

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
        toTimeDifference(view.timeStamp.toString().toLong()){
            time.text = it
        }
    }

    override fun onAttach(view: NotificationView) {
    }

    override fun onDetach() {
    }
}