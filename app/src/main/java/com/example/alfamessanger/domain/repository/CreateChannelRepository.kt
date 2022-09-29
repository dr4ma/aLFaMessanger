package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel

interface CreateChannelRepository {
    fun createChannelId(onSuccess:(channelKey : String) -> Unit)
    fun createChannel(channelId : String, model : ChannelModel, adminModel : UserModel, onSuccess: (model : ChannelModel) -> Unit)
}