package com.example.alfamessanger.domain.usecases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.repository.SearchChannelsRepository
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import javax.inject.Inject

class SearchChannelsUseCase @Inject constructor(private val searchChannelsRepository: SearchChannelsRepository) {

    val listChannels: MutableLiveData<MutableList<ChannelModel>> =
        MutableLiveData<MutableList<ChannelModel>>()
    private lateinit var mObserverLoadAllChannels: Observer<MutableList<ChannelModel>>

    fun getAllChannels(){
        mObserverLoadAllChannels = Observer {
            listChannels.value = it
        }
        searchChannelsRepository.listAllChannels.observe(APP_ACTIVITY_MAIN, mObserverLoadAllChannels)
        searchChannelsRepository.getAllChannels()
    }
}