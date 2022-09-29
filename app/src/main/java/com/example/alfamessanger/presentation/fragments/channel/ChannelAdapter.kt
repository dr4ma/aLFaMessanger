package com.example.alfamessanger.presentation.fragments.channel

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.FeedModel
import com.example.alfamessanger.domain.usecases.ChannelUseCase
import com.example.alfamessanger.presentation.PopupWindowApp
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.PopupTypesOperationChannel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.channels_feed_item.view.*
import kotlinx.android.synthetic.main.item_channels_search_recycler.view.*
import javax.inject.Inject

class ChannelAdapter @Inject constructor(private val channelUseCase: ChannelUseCase) : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {

    private var mListFeed = mutableListOf<FeedModel>()

    inner class ChannelViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val imageBox : CardView = view.image_feed
        var image: ImageView = view.image_feed_channel
        val text: TextView = view.text_feed
        val name: TextView = view.name_admin
        val nameFile: TextView = view.name_file
        val time: TextView = view.time_feed
        val size: TextView = view.size_file
        val fileBox : ConstraintLayout = view.file_box
        val mainBox : ConstraintLayout = view.box_channels_feed

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.channels_feed_item, parent, false)
        return ChannelViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        if (mListFeed[position].iconUri != "") {
            resizeHeightImageForFeed(mListFeed[position].heightImage, holder.image)
            holder.image.downloadAndSetImage(false, mListFeed[position].iconUri)
            holder.image.setOnClickListener {
                ChannelFragment.openFullScreenImage(mListFeed[position].iconUri)
            }
        } else {
            holder.imageBox.visibility = View.GONE
        }
        holder.text.text = mListFeed[position].text
        holder.time.text = mListFeed[position].time.toString().asTime()
        holder.name.text = mListFeed[position].nameAdmin
        if(mListFeed[position].fileUri != ""){
            holder.fileBox.visibility = View.VISIBLE
            holder.nameFile.text = mListFeed[position].nameFile
            holder.size.text = convertBytesToUpper(mListFeed[position].sizeFile)
        }
        holder.mainBox.setOnLongClickListener {
            if(mListFeed[position].adminId == UID){
                PopupWindowApp.createChannelPopup(POPUP_CHANNEL_ADMIN, it){ typeOperation ->
                    when(typeOperation){
                        PopupTypesOperationChannel.DELETE -> {
                            channelUseCase.deleteNews(mListFeed[position]){
                                notifyItemRemoved(position)
                                mListFeed.removeAt(position)
                                if(mListFeed[position].fileUri != ""){
                                    channelUseCase.deleteFileFromStorage(mListFeed[position].fileUri, mListFeed[position].nameFile)
                                }
                                if(mListFeed[position].iconUri != ""){
                                    channelUseCase.deleteImageFromStorage(mListFeed[position].iconUri)
                                }
                            }
                        }
                        PopupTypesOperationChannel.COPY -> {
                            copyTextClipBoard(holder.text.text.toString())
                            showSnackBar(it, APP_ACTIVITY_MAIN.getString(R.string.text_copy), R.color.colorGreen)
                        }
                    }
                }
            }
            else{
                PopupWindowApp.createChannelPopup(POPUP_CHANNEL_SUB, it){ typeOperation ->
                    when(typeOperation){
                        PopupTypesOperationChannel.COPY -> {
                            copyTextClipBoard(holder.text.text.toString())
                            showSnackBar(it, APP_ACTIVITY_MAIN.getString(R.string.text_copy), R.color.colorGreen)
                        }
                    }
                }
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return mListFeed.size
    }

    fun addToList(item: FeedModel) {
        if (!mListFeed.contains(item) && item.feedId != "") {
            mListFeed.add(item)
            notifyItemInserted(mListFeed.size)
        }
    }

    fun clearList(){
        mListFeed.clear()
    }
}