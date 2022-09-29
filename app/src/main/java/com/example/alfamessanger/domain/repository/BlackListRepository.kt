package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.BlackListUserModel
import kotlinx.coroutines.flow.Flow

interface BlackListRepository {

    fun getBlackList(function: () -> Unit): Flow<MutableList<BlackListUserModel>>
    fun removeFromBlackList(model: BlackListUserModel, onSuccess: () -> Unit)
}