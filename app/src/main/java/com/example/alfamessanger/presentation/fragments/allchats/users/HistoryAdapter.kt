package com.example.alfamessanger.presentation.fragments.allchats.users

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.SearchHistoryUserModel
import com.example.alfamessanger.utills.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_users_search_recycler.view.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var mListUsers = mutableListOf<SearchHistoryUserModel>()

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photo: CircleImageView = view.photo_user_search
        val nickName: TextView = view.name_user_search
        val status: TextView = view.status_user_search
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_users_search_recycler, parent, false)
        return HistoryAdapter.HistoryViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
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
            getStatusUser(it){ status ->
                holder.status.text = status
            }
        }
    }

    override fun onViewAttachedToWindow(holder: HistoryViewHolder) {
        holder.itemView.setOnClickListener {
            UsersSearchFragment.openChat(mListUsers[holder.adapterPosition])
        }
    }
    override fun getItemCount(): Int {
        return mListUsers.size
    }

    fun setList(list : List<SearchHistoryUserModel>){
        mListUsers.clear()
        mListUsers.addAll(list)
        notifyDataSetChanged()
    }
}