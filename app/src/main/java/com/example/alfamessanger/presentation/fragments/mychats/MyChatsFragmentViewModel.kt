package com.example.alfamessanger.presentation.fragments.mychats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.MyChatsUseCase
import com.example.alfamessanger.utills.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChatsFragmentViewModel @Inject constructor(private val myChatsUseCase: MyChatsUseCase) : ViewModel() {

    private lateinit var mObserverChats: Observer<MutableList<ChatModel>>
    val chat: MutableLiveData<MutableList<ChatModel>> = MutableLiveData()

    fun getAllChats(function:() -> Unit){
        mObserverChats = Observer {
            chat.value = it
        }
        myChatsUseCase.chat.observe(APP_ACTIVITY_MAIN, mObserverChats)
        myChatsUseCase.getAllChats(){
            function()
        }
    }

    fun getUserModel(){
        viewModelScope.launch {
            myChatsUseCase.getUserModel()
        }
    }

    fun removeObservers(){
        myChatsUseCase.chat.removeObserver(mObserverChats)
        myChatsUseCase.removeObservers()
    }

}