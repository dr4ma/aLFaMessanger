package com.example.alfamessanger.presentation.fragments.myChannels

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.downloadAndSetImage
import com.example.alfamessanger.utills.showToast
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_channels_list.view.*
import kotlinx.android.synthetic.main.item_friends_list.view.*

class MyChannelsAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<MyChannelsAdapter.MyChannelsListViewHolder>(){

    private var mListChannels = mutableListOf<ChannelModel>()

    inner class MyChannelsListViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        var photoChannels: CircleImageView = view.photo_my_channels
        val name: TextView = view.name_channel
        val subscribers: TextView = view.subscribers_channels

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickAdapter(mListChannels[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChannelsListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channels_list, parent, false)
        return MyChannelsListViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyChannelsListViewHolder, position: Int) {
        if (mListChannels[position].iconUrl != "") {
            holder.photoChannels.downloadAndSetImage(true, mListChannels[position].iconUrl)
        } else {
            holder.photoChannels.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
        holder.name.text = mListChannels[position].name
        holder.subscribers.text = APP_ACTIVITY_MAIN.getString(R.string.subs) + " " + mListChannels[position].countSubs
    }

    override fun getItemCount(): Int {
        return mListChannels.size
    }

    fun setList(list: MutableList<ChannelModel>) {
        mListChannels = list
        notifyItemRangeChanged(0, list.size)
    }

    interface OnItemClickListener {
        fun onItemClickAdapter(model: ChannelModel)
    }
}