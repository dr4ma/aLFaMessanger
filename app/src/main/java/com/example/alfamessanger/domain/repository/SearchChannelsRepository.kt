package com.example.alfamessanger.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel

interface SearchChannelsRepository {

    val listAllChannels : MutableLiveData<MutableList<ChannelModel>>
        get() = MutableLiveData()
    fun getAllChannels()
    fun deleteListeners()
}