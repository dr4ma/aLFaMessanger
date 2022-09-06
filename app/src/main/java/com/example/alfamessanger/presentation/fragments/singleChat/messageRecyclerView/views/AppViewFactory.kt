package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views

import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.utills.TYPE_IMAGE
import com.example.alfamessanger.utills.TYPE_TEXT
import com.example.alfamessanger.utills.TYPE_VOICE

class AppViewFactory {

    companion object{
        fun getView(message : MessageModel) : MessageView{
            return when(message.type){
                TYPE_IMAGE -> ViewImageMessage(
                    id = message.id,
                    from = message.from,
                    time_stamp = message.time_stamp,
                    file_url = message.image_url,
                    width = message.width,
                    height = message.height,
                    duration = message.duration
                )
                TYPE_VOICE -> ViewVoiceMessage(
                    id = message.id,
                    from = message.from,
                    time_stamp = message.time_stamp,
                    file_url = message.image_url,
                    duration = message.duration
                )
                TYPE_TEXT -> {
                    ViewTextMessage(
                        id = message.id,
                        from = message.from,
                        time_stamp = message.time_stamp,
                        text = message.text,
                        duration = message.duration
                    )
                }
                else -> {
                    ViewFileMessage(
                        id = message.id,
                        from = message.from,
                        time_stamp = message.time_stamp,
                        text = message.text,
                        size = message.size,
                        file_url = message.image_url,
                        width = message.width,
                        height = message.height,
                        duration = message.duration
                    )
                }
            }
        }
    }
}