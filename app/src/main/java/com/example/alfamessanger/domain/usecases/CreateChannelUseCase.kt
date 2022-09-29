package com.example.alfamessanger.domain.usecases

import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.CreateChannelRepository
import javax.inject.Inject

class CreateChannelUseCase @Inject constructor(private val createChannelRepository: CreateChannelRepository) {

    fun createChannelId(onSuccess:(channelKey : String) -> Unit){
        createChannelRepository.createChannelId {  key ->
            onSuccess(key)
        }
    }

    fun createChannel(channelId : String, adminModel : UserModel, model : ChannelModel, onSuccess: (model : ChannelModel) -> Unit){
        createChannelRepository.createChannel(channelId, model, adminModel){
            onSuccess(it)
        }
    }
}