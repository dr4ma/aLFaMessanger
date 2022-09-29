package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.singleChat.SingleChatAdapter
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView

class AppHolderFactory {

    companion object {
        fun getHolder(model : UserModel, parent: ViewGroup, viewType: Int, function :(view : MessageView) -> Unit): RecyclerView.ViewHolder {
            return when (viewType) {
                MessageView.MESSAGE_IMAGE -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.messages_image_layout, parent, false)
                    HolderImageMessage(model, view)
                }
                MessageView.MESSAGE_VOICE -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.messages_voice_layout, parent, false)
                    HolderVoiceMessage(model, view)
                }
                MessageView.MESSAGE_TEXT -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.messages_text_layout, parent, false)
                    HolderTextMessages(model, view){
                        function(it)
                    }
                }
                else -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.messages_file_layout, parent, false)
                    HolderFileMessage(model, view)
                }
            }
        }
    }
}