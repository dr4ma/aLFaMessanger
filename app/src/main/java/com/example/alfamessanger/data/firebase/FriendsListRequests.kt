package com.example.alfamessanger.data.firebase

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.FriendsListRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener

class FriendsListRequests : FriendsListRepository {

    private var listAllFriends: MutableList<UserModel> = mutableListOf()
    override val friendsList: MutableLiveData<MutableList<UserModel>> = MutableLiveData()
    private lateinit var listenerAllFriends: AppValueEventListener

    override suspend fun getFriends(function: () -> Unit) {

        listenerAllFriends = AppValueEventListener { child ->
            if(child.exists()){
                listAllFriends.clear()
                for(friend in child.children){
                    listAllFriends.add(friend.getValue(UserModel::class.java) ?: UserModel())
                }
                listAllFriends.sortBy { it.name }
                friendsList.value = listAllFriends
            }
        }
        DATABASE_REFERENCE.child(NODE_FRIENDS).child(UID).addValueEventListener(listenerAllFriends)
        DATABASE_REFERENCE.child(NODE_FRIENDS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
              if(!it.exists()){
                  function()
              }
            })
    }
}