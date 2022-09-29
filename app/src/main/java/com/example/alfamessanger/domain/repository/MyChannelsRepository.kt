package com.example.alfamessanger.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel

interface MyChannelsRepository {

    val myChannelsList : MutableLiveData<MutableList<ChannelModel>>
        get() = MutableLiveData()
    suspend fun getMyChannels(onSuccess:() -> Unit)
}