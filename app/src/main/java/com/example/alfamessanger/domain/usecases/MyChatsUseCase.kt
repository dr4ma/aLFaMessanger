package com.example.alfamessanger.domain.usecases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.GetUserModelRepository
import com.example.alfamessanger.domain.repository.MyChatsRepository
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import javax.inject.Inject

class MyChatsUseCase @Inject constructor(private val myChatsRepository: MyChatsRepository, private val userModelRepository: GetUserModelRepository) {

    private lateinit var mObserverChats: Observer<MutableList<ChatModel>>
    val chat: MutableLiveData<MutableList<ChatModel>> = MutableLiveData()

    fun getAllChats(function:() -> Unit){
        mObserverChats = Observer {
            chat.value = it
        }
        myChatsRepository.listAllChatsResult.observe(APP_ACTIVITY_MAIN, mObserverChats)
        myChatsRepository.getAllChats(){
            function()
        }
    }

    suspend fun getUserModel(){
        userModelRepository.getUserModel {  }
    }

    fun removeObservers(){
        myChatsRepository.listAllChatsResult.removeObserver(mObserverChats)
        myChatsRepository.removeListeners()
    }
}