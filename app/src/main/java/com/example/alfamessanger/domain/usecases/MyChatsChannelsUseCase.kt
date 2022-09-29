package com.example.alfamessanger.domain.usecases

import android.util.Log
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.ChannelMyChatsModel
import com.example.alfamessanger.domain.repository.MyChatsChannelsRepository
import com.example.alfamessanger.utills.CHANNELS_LIST
import com.example.alfamessanger.utills.TAG
import com.example.alfamessanger.utills.UID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MyChatsChannelsUseCase @Inject constructor(private val myChatsChannelsRepository: MyChatsChannelsRepository) {

    private val listChannelsResult = mutableListOf<ChannelModel>()
    private val emittingList = mutableListOf<ChannelMyChatsModel>()

    fun getSubscribeChannels() : Flow<MutableList<ChannelMyChatsModel>> = flow{
        myChatsChannelsRepository.getSubscribeChannels()
            .collect {
                emittingList.clear()
                it.forEach { channel ->
                    val listChannels = ArrayList(channel.subscribers.values)
                    run breaking@{
                        listChannels.forEach { sub ->
                            if (sub.uid == UID) {
                                listChannelsResult.add(channel)
                                return@breaking
                            }
                        }
                    }
                }
                listChannelsResult.forEach { channel ->
                    val listFeed = ArrayList(channel.feed.values)
                    val lastNews = listFeed[listFeed.size - 1]
                    val model = ChannelMyChatsModel(
                        channelId = channel.channelId,
                        adminUid = channel.adminUid,
                        name = channel.name,
                        type = channel.type,
                        iconUrl = channel.iconUrl,
                        lastNews = lastNews.text,
                        timeNews = lastNews.time
                    )
                    emittingList.add(model)
                }
                listChannelsResult.clear()
                emit(emittingList)
            }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()

    fun removeListener(){
        myChatsChannelsRepository.removeListener()
    }
}