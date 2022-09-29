package com.example.alfamessanger.presentation.fragments.allchats.users

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.OFFLINE
import com.example.alfamessanger.utills.downloadAndSetImage
import com.example.alfamessanger.utills.getStatusUser
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_users_search_recycler.view.*

class UsersSearchAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<UsersSearchAdapter.UsersSearchViewHolder>() {

    private var mListUsers = emptyList<UserModel>()

    inner class UsersSearchViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var photo: CircleImageView = view.photo_user_search
        val nickName: TextView = view.name_user_search
        val status: TextView = view.status_user_search

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickAdapter(mListUsers[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_users_search_recycler, parent, false)
        return UsersSearchViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: UsersSearchViewHolder, position: Int) {
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
        holder.nickName.text = mListUsers[position].nickname
        holder.status.text = OFFLINE
        mListUsers[position].uid?.let {
            getStatusUser(it) { status ->
                holder.status.text = status
            }
        }
    }

    override fun getItemCount(): Int {
        return mListUsers.size
    }

    fun setList(list: List<UserModel>) {
        mListUsers = list
        notifyItemRangeChanged(0, list.size)
    }

    interface OnItemClickListener {
        fun onItemClickAdapter(model: UserModel)
    }
}