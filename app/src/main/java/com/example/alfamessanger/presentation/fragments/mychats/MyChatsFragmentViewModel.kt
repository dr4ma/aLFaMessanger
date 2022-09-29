package com.example.alfamessanger.presentation.fragments.mychats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.ChannelMyChatsModel
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.MyChatsChannelsUseCase
import com.example.alfamessanger.domain.usecases.MyChatsUseCase
import com.example.alfamessanger.utills.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChatsFragmentViewModel @Inject constructor(private val myChatsUseCase: MyChatsUseCase,
private val myChatsChannelsUseCase: MyChatsChannelsUseCase) : ViewModel() {

    val chat: MutableLiveData<MutableList<ChatModel>> = MutableLiveData()
    val channels: MutableLiveData<MutableList<ChannelMyChatsModel>> = MutableLiveData()

    fun getAllChats(function:() -> Unit) {
        viewModelScope.launch {
            myChatsUseCase.getAllChats(){
                function()
            }
                .collect {
                    chat.postValue(it)
                }
        }
    }

    fun getChannelsSubscribed(){
        viewModelScope.launch {
            myChatsChannelsUseCase.getSubscribeChannels()
                .collect {
                    channels.postValue(it)
                }
        }
    }

    fun getUserModel(){
        viewModelScope.launch {
            myChatsUseCase.getUserModel()
        }
    }

    fun removeObservers(){
        myChatsUseCase.removeObservers()
    }

}