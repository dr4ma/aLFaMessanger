package com.example.alfamessanger.presentation.activities.splashActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.ChannelMyChatsModel
import com.example.alfamessanger.domain.usecases.FeedUseCase
import com.example.alfamessanger.domain.usecases.MyChatsChannelsUseCase
import com.example.alfamessanger.domain.usecases.MyChatsUseCase
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.StatusConnection
import com.example.alfamessanger.utills.networkConnection.CheckInternetConnectionInMoment
import com.example.alfamessanger.utills.networkConnection.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private var feedUseCase: FeedUseCase,
    private val myChatsUseCase: MyChatsUseCase,
    private val checkInternetConnectionInMoment: CheckInternetConnectionInMoment,
    private val myChatsChannelsUseCase: MyChatsChannelsUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    fun getAllData(onSuccess: () -> Unit) {
        getFeedList {
            getChatsList {
                getSubscribeChannels {
                    onSuccess()
                }
            }
        }
    }

    private fun getFeedList(onSuccess: () -> Unit) {
        viewModelScope.launch {
            feedUseCase.getFeedNews {

            }
                .collect {
                    FEED_LIST = it
                    onSuccess()
                }
        }
    }

    private fun getChatsList(onSuccess: () -> Unit) {
        viewModelScope.launch {
            myChatsUseCase.getAllChats {
            }
                .collect {
                    CHAT_LIST = it
                    onSuccess()
                }
        }

    }

    private fun getSubscribeChannels(onSuccess: () -> Unit) {
        CHANNELS_LIST.clear()
        viewModelScope.launch {
            myChatsChannelsUseCase.getSubscribeChannels()
                .collect {
                    CHANNELS_LIST = it
                    onSuccess()
                }
        }
    }

    fun observConnection(function: (status: StatusConnection) -> Unit) {
        viewModelScope.launch {
            val status = connectivityObserver.observe()
            status.collect {
                function(it)
            }
        }
    }

    fun removeListener(){
        myChatsChannelsUseCase.removeListener()
    }

    fun checkConnectionInMoment(): Boolean {
        return checkInternetConnectionInMoment.check()
    }
}