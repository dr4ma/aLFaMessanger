package com.example.alfamessanger.presentation.fragments.allchats.channels

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.usecases.SearchChannelsUseCase
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChannelsSearchViewModel @Inject constructor(private val searchChannelsUseCase: SearchChannelsUseCase) : ViewModel() {

    val listChannels: MutableLiveData<MutableList<ChannelModel>> = MutableLiveData<MutableList<ChannelModel>>()
    private lateinit var mObserverLoadAllChannels: Observer<MutableList<ChannelModel>>
    private val searchList = mutableListOf<ChannelModel>()

    fun searchChannel(name: String, function: (MutableList<ChannelModel>) -> Unit) {
        searchList.clear()
        listChannels.value?.forEach { channel ->
            if (channel.name.lowercase(Locale.getDefault()).contains(name.lowercase(Locale.getDefault()))) {
                if (channel.name == name) {
                    searchList.add(0, channel)
                } else {
                    searchList.add(channel)
                }
            }
        }
        function(searchList)
    }

    fun getAllChannels() {
        mObserverLoadAllChannels = Observer {
            listChannels.value = it
        }
        searchChannelsUseCase.listChannels.observe(APP_ACTIVITY_MAIN, mObserverLoadAllChannels)
        searchChannelsUseCase.getAllChannels()
    }

}