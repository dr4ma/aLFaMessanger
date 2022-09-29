package com.example.alfamessanger.domain.usecases

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedModel
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.ChannelRepository
import com.example.alfamessanger.domain.repository.FileStorageRepository
import com.example.alfamessanger.domain.repository.SearchChannelsRepository
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.TAG
import com.example.alfamessanger.utills.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.io.File
import javax.inject.Inject

class ChannelUseCase @Inject constructor(
    private val channelsRepository: ChannelRepository,
    private val fileStorageRepository: FileStorageRepository
) {

    private lateinit var mObserverFeed: Observer<FeedModel>
    val feed: MutableLiveData<FeedModel> = MutableLiveData()

    fun createFeedId(onSuccess: (channelKey: String) -> Unit) {
        channelsRepository.createFeedId { key ->
            onSuccess(key)
        }
    }

    fun createFeed(channelId: String, model: FeedModel, onSuccess: (model: FeedModel) -> Unit) {
        channelsRepository.createFeed(channelId, model) {
            onSuccess(it)
        }
    }

    fun getFeed(channelId: String, function: () -> Unit) {
        mObserverFeed = Observer {
            feed.value = it
        }
        channelsRepository.feed.observe(APP_ACTIVITY_MAIN, mObserverFeed)
        channelsRepository.getMessages(channelId) {
            function()
        }
    }

    fun getSubs(channelId: String, onSuccess: (MutableList<UserModel>) -> Unit) {
        channelsRepository.getSubs(channelId){
            onSuccess(it)
        }
    }

    fun deleteNews(model: FeedModel, onSuccess: () -> Unit) {
        channelsRepository.deleteNews(model) {
            onSuccess()
        }
    }

    fun deleteFileFromStorage(path: String, fileName: String) {
        val file = File(APP_ACTIVITY_MAIN.filesDir, fileName)
        if (file.exists()) {
            file.delete()
        }
        fileStorageRepository.removeFileFromStorage(path)
    }

    fun deleteImageFromStorage(path: String) {
        fileStorageRepository.removeFileFromStorage(path)
    }

    fun subscribeChannel(channelModel: ChannelModel, userModel: UserModel, function: () -> Unit) {
        channelsRepository.subscribeChannel(channelModel, userModel) {
            function()
        }
    }

    fun unSubscribeChannel(channelModel: ChannelModel, userModel: UserModel, function: () -> Unit) {
        channelsRepository.unSubscribeChannel(channelModel, userModel) {
            function()
        }
    }

    fun deleteChannel(channelId: String, onSuccess: () -> Unit){
        channelsRepository.deleteChannel(channelId){
            onSuccess()
        }
    }

    fun removeObserver() {
        channelsRepository.feed.removeObserver(mObserverFeed)
        channelsRepository.deleteListener()
        feed.value = FeedModel()
    }
}