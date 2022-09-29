package com.example.alfamessanger.presentation.fragments.feed

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.data.firebase.FeedRequests
import com.example.alfamessanger.data.firebase.FileStorageRequests
import com.example.alfamessanger.data.firebase.SingleChatRequests
import com.example.alfamessanger.domain.models.FeedModel
import com.example.alfamessanger.domain.models.FeedNewsModel
import com.example.alfamessanger.domain.repository.FeedRepository
import com.example.alfamessanger.domain.repository.FileStorageRepository
import com.example.alfamessanger.domain.repository.SingleChatRepository
import com.example.alfamessanger.domain.usecases.ChannelUseCase
import com.example.alfamessanger.domain.usecases.FeedUseCase
import com.example.alfamessanger.domain.usecases.SingleChatUseCase
import com.example.alfamessanger.presentation.PopupWindowApp
import com.example.alfamessanger.presentation.fragments.channel.ChannelFragment
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.PopupTypesOperationChannel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.channels_feed_item.view.*
import kotlinx.android.synthetic.main.channels_feed_item.view.file_box
import kotlinx.android.synthetic.main.channels_feed_item.view.image_feed
import kotlinx.android.synthetic.main.channels_feed_item.view.image_feed_channel
import kotlinx.android.synthetic.main.channels_feed_item.view.name_admin
import kotlinx.android.synthetic.main.channels_feed_item.view.name_file
import kotlinx.android.synthetic.main.channels_feed_item.view.size_file
import kotlinx.android.synthetic.main.channels_feed_item.view.text_feed
import kotlinx.android.synthetic.main.channels_feed_item.view.time_feed
import kotlinx.android.synthetic.main.news_feed_item.view.*
import javax.inject.Inject

class FeedAdapter @Inject constructor(
    private val feedUseCase: FeedUseCase,
    private val channelUseCase: ChannelUseCase
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private var mListFeed = mutableListOf<FeedNewsModel>()

    inner class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageBox: CardView = view.image_feed
        var image: ImageView = view.image_feed_channel
        val text: TextView = view.text_feed
        val name: TextView = view.name_admin
        val nameFile: TextView = view.name_file
        val time: TextView = view.time_feed
        val size: TextView = view.size_file
        val fileBox: ConstraintLayout = view.file_box
        val channelIcon: CircleImageView = view.photo_channel_news
        val channelName: TextView = view.channel_name_news
        val fileProgress: ProgressBar = view.file_progress
        val menu: ImageView = view.news_feed_menu
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_feed_item, parent, false)
        return FeedViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        if (mListFeed[position].iconUri != "") {
            resizeHeightImageForFeed(mListFeed[position].heightImage, holder.image)
            holder.image.downloadAndSetImage(false, mListFeed[position].iconUri)
            holder.image.setOnClickListener {
                FeedFragment.openFullScreenImage(mListFeed[position].iconUri)
            }
        } else {
            holder.imageBox.visibility = View.GONE
        }
        holder.text.text = mListFeed[position].text
        holder.time.text = mListFeed[position].time.toString().asTime()
        holder.name.text = mListFeed[position].nameAdmin
        if (mListFeed[position].fileUri != "") {
            holder.fileBox.visibility = View.VISIBLE
            holder.nameFile.text = mListFeed[position].nameFile
            holder.size.text = convertBytesToUpper(mListFeed[position].sizeFile)
        }
        if (mListFeed[position].channelIcon != "") {
            holder.channelIcon.downloadAndSetImage(true, mListFeed[position].channelIcon)
        } else {
            holder.channelIcon.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
        holder.channelName.text = mListFeed[position].channelName
        holder.channelIcon.setOnClickListener {
            feedUseCase.getChannelModel(mListFeed[position].channelId) {
                FeedFragment.openChannel(it)
            }
        }
        holder.fileBox.setOnClickListener {
            holder.fileProgress.visibility = View.VISIBLE
            feedUseCase.downloadFeedFile(
                mListFeed[position].fileUri,
                mListFeed[position].nameFile
            ) {
                holder.fileProgress.visibility = View.GONE
            }
        }
        holder.menu.setOnClickListener {
            if (mListFeed[position].adminId == UID) {
                PopupWindowApp.createChannelPopup(POPUP_CHANNEL_ADMIN, it) { typeOperation ->
                    when (typeOperation) {
                        PopupTypesOperationChannel.DELETE -> {
                            feedUseCase.deleteNews(mListFeed[position]) {
                                notifyItemRemoved(position)
                                mListFeed.removeAt(position)
                                if (mListFeed[position].fileUri != "") {
                                    channelUseCase.deleteFileFromStorage(
                                        mListFeed[position].fileUri,
                                        mListFeed[position].nameFile
                                    )
                                }
                                if (mListFeed[position].iconUri != "") {
                                    channelUseCase.deleteImageFromStorage(mListFeed[position].iconUri)
                                }
                            }
                        }
                        PopupTypesOperationChannel.COPY -> {
                            copyTextClipBoard(holder.text.text.toString())
                            showSnackBar(
                                it,
                                APP_ACTIVITY_MAIN.getString(R.string.text_copy),
                                R.color.colorGreen
                            )
                        }
                    }
                }
            } else {
                PopupWindowApp.createChannelPopup(POPUP_CHANNEL_SUB, it) { typeOperation ->
                    when (typeOperation) {
                        PopupTypesOperationChannel.COPY -> {
                            copyTextClipBoard(holder.text.text.toString())
                            showSnackBar(
                                it,
                                APP_ACTIVITY_MAIN.getString(R.string.text_copy),
                                R.color.colorGreen
                            )
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mListFeed.size
    }

    fun setList(list: MutableList<FeedNewsModel>) {
        if (!mListFeed.containsAll(list)) {
            mListFeed = list
            notifyItemRangeChanged(0, mListFeed.size)
        } else {
            var pos = -1
            if (mListFeed.size > list.size) {
                mListFeed.forEach { item ->
                    pos++
                    if(!list.contains(item)){
                        notifyItemRemoved(pos)
                        pos--
                    }
                }
                mListFeed = list
            }
        }
    }

}