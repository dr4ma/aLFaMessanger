package com.example.alfamessanger.presentation.fragments.mychats

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.ChannelMyChatsModel
import com.example.alfamessanger.domain.usecases.FeedUseCase
import com.example.alfamessanger.presentation.fragments.allchats.users.UsersSearchFragment
import com.example.alfamessanger.utills.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_my_chats_channels_recycler.view.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyChatsChannelsAdapter @Inject constructor(private val feedUseCase: FeedUseCase) :
    RecyclerView.Adapter<MyChatsChannelsAdapter.MyChatsChannelsViewHolder>() {

    private var mListChannel = mutableListOf<ChannelMyChatsModel>()

    inner class MyChatsChannelsViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var photo: CircleImageView = view.photo_my_channels
        val name: TextView = view.name_my_channels
        val lastNews: TextView = view.last_news
        val timeNews: TextView = view.time_channel_my_chats
        val iconTypeChannels: ImageView = view.type_channel

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChatsChannelsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_chats_channels_recycler, parent, false)
        return MyChatsChannelsViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyChatsChannelsViewHolder, position: Int) {
        if (mListChannel[position].iconUrl != "") {
            holder.photo.downloadAndSetImage(true, mListChannel[position].iconUrl)
        } else {
            holder.photo.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
        holder.name.text = mListChannel[position].name
        holder.lastNews.text = mListChannel[position].lastNews
        toTimeDifference(mListChannel[position].timeNews.toString().toLong()){
            holder.timeNews.text = it
        }
        when (mListChannel[position].type) {
            PRIVATE -> {
                holder.iconTypeChannels.setBackgroundResource(R.drawable.ic_lock_open)
            }
            PUBLIC -> {
                holder.iconTypeChannels.setBackgroundResource(R.drawable.ic_lock_close)
            }
            CLOSE -> {
                holder.iconTypeChannels.setBackgroundResource(R.drawable.ic_lock_hide)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: MyChatsChannelsViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            feedUseCase.getChannelModel(mListChannel[holder.adapterPosition].channelId){
                MyChatsFragment.openChannel(it)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: MyChatsChannelsViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return mListChannel.size
    }

    fun setList(list: MutableList<ChannelMyChatsModel>) {
        try{
            list.sortBy { it.timeNews.toString() }
            mListChannel = list.asReversed()
            notifyDataSetChanged()
        }
        catch (e : Exception){
            Log.e(TAG, e.message.toString())
        }

    }

}