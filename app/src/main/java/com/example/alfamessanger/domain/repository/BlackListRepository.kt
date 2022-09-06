package com.example.alfamessanger.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.models.UserModel

interface BlackListRepository {

    val blackList : MutableLiveData<MutableList<BlackListUserModel>>
        get() = MutableLiveData()
    fun getBlackList(function:() -> Unit)
    fun removeFromBlackList(model : BlackListUserModel, onSuccess : () -> Unit)
    fun removeListener()
}