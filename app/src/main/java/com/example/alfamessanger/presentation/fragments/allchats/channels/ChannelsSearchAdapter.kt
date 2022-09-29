package com.example.alfamessanger.presentation.fragments.allchats.channels

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.allchats.users.UsersSearchAdapter
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.TAG
import com.example.alfamessanger.utills.downloadAndSetImage
import com.example.alfamessanger.utills.showToast
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_channels_search_recycler.view.*
import kotlinx.android.synthetic.main.item_users_search_recycler.view.*

class ChannelsSearchAdapter (private val listener: ChannelsSearchAdapter.OnItemClickListener) :
    RecyclerView.Adapter<ChannelsSearchAdapter.ChannelsSearchViewHolder>(){

    private var mListChannels = mutableListOf<ChannelModel>()

    inner class ChannelsSearchViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var photoChannel: CircleImageView = view.photo_channel_search
        val name: TextView = view.name_channel_search

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickAdapter(mListChannels[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelsSearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channels_search_recycler, parent, false)
        return ChannelsSearchViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ChannelsSearchViewHolder, position: Int) {
        if (mListChannels[position].iconUrl != "") {
            holder.photoChannel.downloadAndSetImage(true, mListChannels[position].iconUrl)
        } else {
            holder.photoChannel.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
//        holder.photoChannel.setOnClickListener {
//            val list = ArrayList(mListChannels[position].feed.values)
//            showToast(list[0].text)
//        }
        holder.name.text = mListChannels[position].name
    }

    override fun getItemCount(): Int {
       return mListChannels.size
    }

    fun setList(list: MutableList<ChannelModel>) {
        mListChannels = list
        notifyItemRangeChanged(0, list.size)
    }

    fun clearList(){
        mListChannels.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClickAdapter(model: ChannelModel)
    }
}