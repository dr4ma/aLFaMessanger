package com.example.alfamessanger.data.firebase

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.SearchChannelsRepository
import com.example.alfamessanger.utills.DATABASE_REFERENCE
import com.example.alfamessanger.utills.NODE_CHANNELS
import com.example.alfamessanger.utills.NODE_USERS
import com.example.alfamessanger.utills.listeners.AppValueEventListener

class SearchChannelsRequests : SearchChannelsRepository {

    override val listAllChannels: MutableLiveData<MutableList<ChannelModel>> = MutableLiveData()
    private lateinit var listenerAllChannels: AppValueEventListener
    private var listChannels: MutableList<ChannelModel> = mutableListOf()

    override fun getAllChannels() {
        listenerAllChannels = AppValueEventListener { data ->
            if (data.exists()) {
                listChannels.clear()
                for (user in data.children) {
                    listChannels.add(user.getValue(ChannelModel::class.java) ?: ChannelModel())
                }
                listAllChannels.value = listChannels
            }
        }
        DATABASE_REFERENCE.child(NODE_CHANNELS).addValueEventListener(listenerAllChannels)
    }

    override fun deleteListeners() {
        DATABASE_REFERENCE.child(NODE_USERS).removeEventListener(listenerAllChannels)
    }
}