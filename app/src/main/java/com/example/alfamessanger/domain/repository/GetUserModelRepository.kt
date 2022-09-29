package com.example.alfamessanger.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.UserModel

interface GetUserModelRepository {

    val listAllUsersResult : MutableLiveData<MutableList<UserModel>>
        get() = MutableLiveData()
    suspend fun getUserModel(onSuccess:() -> Unit)
    fun getOtherUserModel(id : String, onSuccess:(model : UserModel) -> Unit)
    fun saveNewUserData(mapNewData : Map<String, String>, onSuccess:() -> Unit)
    fun getAllUsers(onSuccess: () -> Unit)
    fun setPrivacyAccount(map : Map<String, Boolean>)
    fun deleteListeners() {}
}