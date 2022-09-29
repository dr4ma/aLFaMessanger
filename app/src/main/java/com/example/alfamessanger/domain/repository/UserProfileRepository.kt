package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.models.UserModel

interface UserProfileRepository {

    fun addInFriends(friendModel : UserModel, onSuccess: () -> Unit)
    fun deleteFromFriends(friendModel : UserModel, onSuccess: () -> Unit)
    fun deleteMeFromFriends(friendModel : UserModel)
    fun deleteFromBlock(model : UserModel, onSuccess: () -> Unit)
    fun checkFriendOrNot(friendModel : UserModel, onSuccess: (result : Boolean) -> Unit)
    fun checkBlockOrNot(model : UserModel, onSuccess: (result : Boolean) -> Unit)
    fun addInBlock(blockModel : BlackListUserModel, onSuccess: () -> Unit)
}