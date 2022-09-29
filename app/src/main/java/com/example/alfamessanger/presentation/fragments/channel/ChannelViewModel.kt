package com.example.alfamessanger.presentation.fragments.channel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.ChannelUseCase
import com.example.alfamessanger.domain.usecases.LoadPicUseCase
import com.example.alfamessanger.utills.*
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val loadPicUseCase: LoadPicUseCase,
    private val channelUseCase: ChannelUseCase
) : ViewModel() {

    private lateinit var model: FeedModel
    private lateinit var mObserverFeed: Observer<FeedModel>
    val feed: MutableLiveData<FeedModel> = MutableLiveData()
    private var subs: MutableList<UserModel> = mutableListOf()

    fun uploadChannelNewsFile(
        channelId: String,
        model: FeedModel,
        fileUrl: Uri,
        path: StorageReference
    ) {
        loadPicUseCase.loadFileFeed(channelId, model, fileUrl, path)
    }

    fun uploadChannelNewsImage(
        channelId: String,
        model: FeedModel,
        iconUrl: Uri,
        path: StorageReference
    ) {
        loadPicUseCase.loadPicFeed(channelId, model, iconUrl, path)
    }

    fun sendNewsChannel(
        sizeFile: Double,
        heightImage: Int,
        channelId: String,
        text: String,
        iconUri: String,
        fileUri: String,
        name: String,
        onSuccess: (model: FeedModel) -> Unit
    ) {
        val time = ServerValue.TIMESTAMP
        channelUseCase.createFeedId { key ->
            model = FeedModel(
                channelId = channelId,
                adminId = UID,
                feedId = key,
                iconUri = iconUri,
                fileUri = fileUri,
                text = text,
                time = time,
                nameFile = name,
                nameAdmin = USER.name,
                heightImage = heightImage,
                sizeFile = sizeFile
            )
        }
        channelUseCase.createFeed(channelId, model) {
            onSuccess(it)
        }
    }

    fun getFeed(channelId: String, function: () -> Unit) {
        mObserverFeed = Observer {
            feed.value = it
        }
        channelUseCase.feed.observe(APP_ACTIVITY_MAIN, mObserverFeed)
        channelUseCase.getFeed(channelId) {
            function()
        }
    }

    private fun getSubs(channelId: String, onSuccess: (MutableList<UserModel>) -> Unit) {
        channelUseCase.getSubs(channelId) {
            onSuccess(it)
        }
    }

    fun subscribeChannel(channelModel: ChannelModel, userModel: UserModel, function: () -> Unit) {
        channelUseCase.subscribeChannel(channelModel, userModel) {
            function()
        }
    }

    fun unSubscribeChannel(channelModel: ChannelModel, userModel: UserModel, function: () -> Unit) {
        channelUseCase.unSubscribeChannel(channelModel, userModel) {
            function()
        }
    }

    fun deleteChannel(channelId: String, onSuccess: () -> Unit) {
        channelUseCase.deleteChannel(channelId) {
            onSuccess()
        }
    }

    fun checkSubOrNot(channelId: String, onSuccess: (sub: Boolean) -> Unit) {
        getSubs(channelId){
            var result = false
            run breaking@{
                it.forEach { user ->
                    if (user.uid == UID) {
                        result = true
                        return@breaking
                    }
                }
            }
            onSuccess(result)
        }
    }

    fun removeObserver() {
        channelUseCase.feed.removeObserver(mObserverFeed)
        channelUseCase.removeObserver()
        feed.value = FeedModel()
    }
}