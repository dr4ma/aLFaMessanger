package com.example.alfamessanger.presentation.fragments.subscribers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.friendsList.FriendsListAdapter
import com.example.alfamessanger.presentation.fragments.friendsList.FriendsListFragment
import com.example.alfamessanger.utills.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_friends_list.view.*
import kotlinx.android.synthetic.main.item_subs_list.view.*

class SubscribersAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<SubscribersAdapter.SubscribersViewHolder>() {

    private var mListSubs = mutableListOf<UserModel>()
    private var adminIdStr = ""

    inner class SubscribersViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        var photo: CircleImageView = view.photo_user_sub
        val nickName: TextView = view.name_user_sub
        val status: TextView = view.status_user_sub
        val crown: ImageView = view.admin_icon

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickAdapter(mListSubs[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subs_list, parent, false)
        return SubscribersViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: SubscribersViewHolder, position: Int) {
        if (mListSubs[position].iconUrl != "") {
            holder.photo.downloadAndSetImage(true, mListSubs[position].iconUrl)
        } else {
            holder.photo.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
        if(mListSubs[position].uid == adminIdStr){
            holder.crown.visibility = View.VISIBLE
        }
        holder.nickName.text = mListSubs[position].name
        holder.status.text = OFFLINE
        mListSubs[position].uid?.let {
            getStatusUser(it) { status ->
                holder.status.text = status
            }
        }
    }

    override fun getItemCount(): Int {
        return mListSubs.size
    }

    fun setList(adminId : String, list: MutableList<UserModel>) {
        adminIdStr = adminId
        mListSubs = list
        notifyItemRangeChanged(0, list.size)
    }

    interface OnItemClickListener {
        fun onItemClickAdapter(model: UserModel)
    }
}