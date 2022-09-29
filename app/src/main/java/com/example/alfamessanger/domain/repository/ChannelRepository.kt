package com.example.alfamessanger.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedModel
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {

    fun createFeedId(onSuccess:(channelKey : String) -> Unit)
    fun createFeed(channelId : String, model : FeedModel, onSuccess: (model : FeedModel) -> Unit)
    fun getMessages(channelId : String, function:() -> Unit)
    fun deleteListener()
    fun deleteNews(model : FeedModel, onSuccess: () -> Unit)
    fun deleteChannel(channelId: String, onSuccess: () -> Unit)
    fun getSubs(channelId: String, onSuccess: (MutableList<UserModel>) -> Unit )
    fun subscribeChannel(channelModel: ChannelModel, userModel : UserModel, function:() -> Unit)
    fun unSubscribeChannel(channelModel: ChannelModel, userModel : UserModel, function:() -> Unit)
    val feed: MutableLiveData<FeedModel> get() = MutableLiveData()
}