package com.example.alfamessanger.presentation.fragments.createChannel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.CreateChannelUseCase
import com.example.alfamessanger.domain.usecases.LoadPicUseCase
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateChannelViewModel @Inject constructor(
    private val createChannelUseCase: CreateChannelUseCase,
    private val loadPicUseCase: LoadPicUseCase
) :
    ViewModel() {

    private lateinit var channelKeyStr: String
    private lateinit var model: ChannelModel

    fun createChannel(
        adminModel: UserModel,
        type: String,
        name: String,
        description: String,
        iconUrl: String,
        onSuccess: (model : ChannelModel) -> Unit
    ) {
        createChannelUseCase.createChannelId { channelKey ->
            channelKeyStr = channelKey
            model = ChannelModel(
                channelId = channelKey,
                adminUid = adminModel.uid.toString(),
                type = type,
                name = name,
                description = description,
                iconUrl = iconUrl,
                countSubs = 1
            )
        }
        createChannelUseCase.createChannel(channelKeyStr, adminModel, model) {
            onSuccess(it)
        }
    }

    fun uploadChannelIcon(model : ChannelModel, uri: Uri, path: StorageReference){
        loadPicUseCase.loadPicChannel(model, uri, path)
    }
}