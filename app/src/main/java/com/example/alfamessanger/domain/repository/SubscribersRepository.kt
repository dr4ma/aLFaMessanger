package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface SubscribersRepository {

    fun getSubs(channelId: String): Flow<MutableList<UserModel>>

}