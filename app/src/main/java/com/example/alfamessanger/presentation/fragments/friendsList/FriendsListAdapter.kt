package com.example.alfamessanger.presentation.fragments.friendsList

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.OFFLINE
import com.example.alfamessanger.utills.downloadAndSetImage
import com.example.alfamessanger.utills.getStatusUser
import com.google.android.material.shape.MaterialShapeDrawable
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_black_list_recycler.view.*
import kotlinx.android.synthetic.main.item_friends_list.view.*

class FriendsListAdapter(private val listener: FriendsListAdapter.OnItemClickListener) :
    RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder>() {

    private var mListUsers = mutableListOf<UserModel>()

    inner class FriendsListViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        var photo: CircleImageView = view.photo_user_friend
        val nickName: TextView = view.name_user_friend
        val status: TextView = view.status_user_friend
        val goToChat: ImageView = view.go_to_chat

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickAdapter(mListUsers[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friends_list, parent, false)
        return FriendsListViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: FriendsListViewHolder, position: Int) {
        if (mListUsers[position].iconUrl != "") {
            holder.photo.downloadAndSetImage(true, mListUsers[position].iconUrl)
        } else {
            holder.photo.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
        holder.nickName.text = mListUsers[position].name
        holder.status.text = OFFLINE
        mListUsers[position].uid?.let {
            getStatusUser(it) { status ->
                holder.status.text = status
            }
        }

        holder.goToChat.setOnClickListener {
            FriendsListFragment.goToChat(mListUsers[position])
        }
    }

    override fun getItemCount(): Int {
        return mListUsers.size
    }

    fun setList(list: MutableList<UserModel>) {
        mListUsers = list
        notifyItemRangeChanged(0, list.size)
    }

    interface OnItemClickListener {
        fun onItemClickAdapter(model: UserModel)
    }
}