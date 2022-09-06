package com.example.alfamessanger.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.UserModel

interface FriendsListRepository {

    val friendsList : MutableLiveData<MutableList<UserModel>>
        get() = MutableLiveData()
    suspend fun getFriends(function:() -> Unit)
}