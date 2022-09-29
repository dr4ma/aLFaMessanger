package com.example.alfamessanger.domain.usecases

import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.repository.BlackListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BlackListUseCase @Inject constructor(private val blackListRepository: BlackListRepository) {

    fun getBlackList(function: () -> Unit): Flow<MutableList<BlackListUserModel>> = flow {
        blackListRepository.getBlackList{
            function()
        }.collect {
            emit(it)
        }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()

    fun removeFromBlackList(model: BlackListUserModel, onSuccess: () -> Unit) {
        blackListRepository.removeFromBlackList(model) {
            onSuccess()
        }
    }
}