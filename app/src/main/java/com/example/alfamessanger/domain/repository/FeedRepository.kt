package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedNewsModel
import kotlinx.coroutines.flow.Flow

interface FeedRepository {

    fun getFeedNews(onSuccess:() -> Unit): Flow<MutableList<ChannelModel>>
    fun getChannelModel(channelId : String, onSuccess: (model : ChannelModel) -> Unit)
    fun deleteNews(model : FeedNewsModel, onSuccess: () -> Unit)
    fun getForNewNotifications(onSuccess: (newNotifications : Boolean) -> Unit)
    fun watchNotifications(map : Map<String, Boolean>)
}