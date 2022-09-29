package com.example.alfamessanger.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface MyChatsRepository {

    val chat : MutableLiveData<ChatModel>
        get() = MutableLiveData()
    val listAllChatsResult : MutableLiveData<MutableList<ChatModel>>
        get() = MutableLiveData()
    fun getAllChats(function:() -> Unit) : Flow<MutableList<ChatModel>>
    fun removeListeners()
}