package com.example.alfamessanger.data.firebase

import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.repository.MyChatsChannelsRepository
import com.example.alfamessanger.utills.DATABASE_REFERENCE
import com.example.alfamessanger.utills.NODE_CHANNELS
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MyChatsChannelsRequests : MyChatsChannelsRepository {

    private var listChannels: MutableList<ChannelModel> = mutableListOf()
    private lateinit var listenerChannels: AppValueEventListener

    override fun getSubscribeChannels(): Flow<MutableList<ChannelModel>> = callbackFlow {
        listenerChannels = AppValueEventListener { child ->
            if (child.exists()) {
                listChannels.clear()
                for (channel in child.children) {
                    listChannels.add(channel.getValue(ChannelModel::class.java) ?: ChannelModel())
                }
               launch {
                   send(listChannels)
               }
            }
        }
        DATABASE_REFERENCE.child(NODE_CHANNELS).addValueEventListener(listenerChannels)
        awaitClose()
    }.distinctUntilChanged()

    override fun removeListener() {
        DATABASE_REFERENCE.child(NODE_CHANNELS).removeEventListener(listenerChannels)
    }
}