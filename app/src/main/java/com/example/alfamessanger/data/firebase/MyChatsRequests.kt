package com.example.alfamessanger.data.firebase

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.repository.MyChatsRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import com.google.firebase.database.ChildEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MyChatsRequests : MyChatsRepository {

    private lateinit var mListenerGetMessages: ChildEventListener
    private lateinit var listenerAllChats: AppValueEventListener
    override val chat: MutableLiveData<ChatModel> = MutableLiveData()
    private var listAllChats: MutableList<ChatModel> = mutableListOf()
    override val listAllChatsResult: MutableLiveData<MutableList<ChatModel>> = MutableLiveData()

    override fun getAllChats(function: () -> Unit) : Flow<MutableList<ChatModel>> = callbackFlow {

        listenerAllChats = AppValueEventListener { data ->
            if (data.exists()) {
                listAllChats.clear()
                for (chat in data.children) {
                    listAllChats.add(chat.getValue(ChatModel::class.java) ?: ChatModel())
                }
                listAllChats.sortBy { it.time_stamp.toString() }
                listAllChats.reverse()
                //listAllChatsResult.value = listAllChats
            }
            launch {
                send(listAllChats)
            }
        }
        DATABASE_REFERENCE.child(NODE_CHATS).child(UID).addValueEventListener(listenerAllChats)
        DATABASE_REFERENCE.child(NODE_CHATS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                if(!it.exists()){
                    function()
                }
            })
        awaitClose()
    }.distinctUntilChanged()

    override fun removeListeners(){
        DATABASE_REFERENCE.child(NODE_CHATS).child(UID).removeEventListener(listenerAllChats)
    }
}