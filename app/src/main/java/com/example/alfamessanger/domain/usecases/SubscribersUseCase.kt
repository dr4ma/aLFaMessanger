package com.example.alfamessanger.domain.usecases

import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.SubscribersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SubscribersUseCase @Inject constructor(private val subscribersRepository: SubscribersRepository) {

    fun getSubs(channelId: String) : Flow<MutableList<UserModel>> = flow{
        subscribersRepository.getSubs(channelId)
            .collect {
                emit(it)
            }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()
}