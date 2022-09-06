package com.example.alfamessanger.presentation.fragments.mychats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.allchats.users.UsersSearchAdapter
import com.example.alfamessanger.utills.*
import com.google.firebase.database.ServerValue
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_my_chats_recycler.view.*
import kotlinx.android.synthetic.main.item_users_search_recycler.view.*

class MyChatsAdapter(private val listener: MyChatsAdapter.OnItemClickListener) :
    RecyclerView.Adapter<MyChatsAdapter.MyChatsViewHolder>() {

    private var mListChat = mutableListOf<ChatModel>()

    inner class MyChatsViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var photo: CircleImageView = view.photo_user_chats
        val name: TextView = view.name_user_chats
        val lastMessage: TextView = view.last_message
        val yourMessage: TextView = view.your_message
        val timeMessage: TextView = view.time_message

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickAdapter(mListChat[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_chats_recycler, parent, false)
        return MyChatsViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyChatsViewHolder, position: Int) {
        if (mListChat[position].from == UID) {
            holder.yourMessage.visibility = View.VISIBLE
        } else {
            holder.yourMessage.visibility = View.GONE
        }
        if (mListChat[position].iconUrl != "") {
            holder.photo.downloadAndSetImage(true, mListChat[position].iconUrl)
        } else {
            holder.photo.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
        holder.name.text = mListChat[position].name
        holder.lastMessage.text = mListChat[position].text
        toTimeDifference(mListChat[position].time_stamp.toString().toLong()){
            holder.timeMessage.text = it
        }
    }

    override fun getItemCount(): Int {
        return mListChat.size
    }

    fun setList(list: MutableList<ChatModel>) {
        mListChat = list
        notifyItemChanged(0, mListChat.size)
//        if (!mListChat.contains(item)) {
//            mListChat.add(item)
//            mListChat.sortBy { it.time_stamp.toString() }
//            notifyItemInserted(mListChat.size)
//        }
    }

    interface OnItemClickListener {
        fun onItemClickAdapter(model: ChatModel)
    }
}