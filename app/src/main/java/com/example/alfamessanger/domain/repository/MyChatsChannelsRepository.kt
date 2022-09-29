package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.ChannelModel
import kotlinx.coroutines.flow.Flow

interface MyChatsChannelsRepository {

    fun getSubscribeChannels()  : Flow<MutableList<ChannelModel>>
    fun removeListener()
}