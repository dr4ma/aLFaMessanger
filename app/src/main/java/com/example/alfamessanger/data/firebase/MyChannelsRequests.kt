package com.example.alfamessanger.data.firebase

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.repository.MyChannelsRepository
import com.example.alfamessanger.utills.DATABASE_REFERENCE
import com.example.alfamessanger.utills.NODE_CHANNELS
import com.example.alfamessanger.utills.UID
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import java.lang.Exception

class MyChannelsRequests : MyChannelsRepository {

    private var listChannels: MutableList<ChannelModel> = mutableListOf()
    override val myChannelsList: MutableLiveData<MutableList<ChannelModel>> = MutableLiveData()
    private lateinit var listenerMyChannels: AppValueEventListener

    override suspend fun getMyChannels(onSuccess: () -> Unit) {
        listenerMyChannels = AppValueEventListener { child ->
            try{
                if(child.exists()){
                    listChannels.clear()
                    for(channel in child.children){
                        if(channel.getValue(ChannelModel::class.java)?.adminUid == UID){
                            listChannels.add(channel.getValue(ChannelModel::class.java) ?: ChannelModel())
                        }
                    }
                    listChannels.sortBy { it.name }
                    myChannelsList.value = listChannels
                }
            }
            catch (e : Exception){

            }
        }
        DATABASE_REFERENCE.child(NODE_CHANNELS).addValueEventListener(listenerMyChannels)
    }
}