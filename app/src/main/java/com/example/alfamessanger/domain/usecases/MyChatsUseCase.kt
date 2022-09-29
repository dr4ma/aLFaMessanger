package com.example.alfamessanger.domain.usecases

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.repository.GetUserModelRepository
import com.example.alfamessanger.domain.repository.MyChatsRepository
import com.example.alfamessanger.utills.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MyChatsUseCase @Inject constructor(private val myChatsRepository: MyChatsRepository, private val userModelRepository: GetUserModelRepository) {

    fun getAllChats(function:() -> Unit) : Flow<MutableList<ChatModel>> = flow{
        myChatsRepository.getAllChats(){
            function()
        }
            .collect {
                emit(it)
            }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()

    suspend fun getUserModel(){
        userModelRepository.getUserModel {  }
    }

    fun removeObservers(){
        myChatsRepository.removeListeners()
    }
}