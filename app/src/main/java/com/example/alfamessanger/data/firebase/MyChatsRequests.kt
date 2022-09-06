package com.example.alfamessanger.data.firebase

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.repository.MyChatsRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import com.google.firebase.database.ChildEventListener

class MyChatsRequests : MyChatsRepository {

    private lateinit var mListenerGetMessages: ChildEventListener
    private lateinit var listenerAllChats: AppValueEventListener
    override val chat: MutableLiveData<ChatModel> = MutableLiveData()
    private var listAllChats: MutableList<ChatModel> = mutableListOf()
    override val listAllChatsResult: MutableLiveData<MutableList<ChatModel>> = MutableLiveData()

    override fun getAllChats(function: () -> Unit) {

        listenerAllChats = AppValueEventListener { data ->
            if (data.exists()) {
                listAllChats.clear()
                for (chat in data.children) {
                    listAllChats.add(chat.getValue(ChatModel::class.java) ?: ChatModel())
                }
                listAllChats.sortBy { it.time_stamp.toString() }
                listAllChats.reverse()
                listAllChatsResult.value = listAllChats
            }
        }
        DATABASE_REFERENCE.child(NODE_CHATS).child(UID).addValueEventListener(listenerAllChats)
        DATABASE_REFERENCE.child(NODE_CHATS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                if(!it.exists()){
                    function()
                }
            })
//        mListenerGetMessages = object : ChildEventListener {
//            override fun onChildAdded(item: DataSnapshot, previousChildName: String?) {
//                if (item.exists()) {
//                    chat.value = item.getValue(ChatModel::class.java)
//                    chat.value?.let { showToast(it?.text) }
//                }
//            }
//
//            override fun onChildChanged(item: DataSnapshot, previousChildName: String?) {
//
//            }
//
//            override fun onChildRemoved(item: DataSnapshot) {
//
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e(TAG_SINGLE_CHAT, "Get message error: " + error.message)
//            }
//        }
//        DATABASE_REFERENCE.child(NODE_CHATS).child(UID).removeEventListener(mListenerGetMessages)
//        DATABASE_REFERENCE.child(NODE_CHATS).child(UID).addChildEventListener(mListenerGetMessages)
    }

    override fun removeListeners(){
        DATABASE_REFERENCE.child(NODE_CHATS).child(UID).removeEventListener(listenerAllChats)
    }
}