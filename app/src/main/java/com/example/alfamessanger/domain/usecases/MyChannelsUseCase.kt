package com.example.alfamessanger.domain.usecases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.repository.MyChannelsRepository
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import javax.inject.Inject

class MyChannelsUseCase @Inject constructor(private val myChannelsRepository: MyChannelsRepository) {

    private lateinit var mObserverChannels: Observer<MutableList<ChannelModel>>
    val channels: MutableLiveData<MutableList<ChannelModel>> = MutableLiveData()

    suspend fun getMyChannels(onSuccess:() -> Unit){
        mObserverChannels = Observer {
            channels.value = it
        }
        myChannelsRepository.myChannelsList.observe(APP_ACTIVITY_MAIN, mObserverChannels)
        myChannelsRepository.getMyChannels{
            onSuccess()
        }
    }
}