package com.example.alfamessanger.domain.usecases

import android.os.Environment
import android.util.Log
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedModel
import com.example.alfamessanger.domain.models.FeedNewsModel
import com.example.alfamessanger.domain.repository.FeedRepository
import com.example.alfamessanger.domain.repository.FileStorageRepository
import com.example.alfamessanger.utills.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.io.File
import javax.inject.Inject

class FeedUseCase @Inject constructor(private val feedRepository: FeedRepository,
private val fileStorageRepository: FileStorageRepository) {

    fun getFeedNews(onSuccess: () -> Unit): Flow<MutableList<FeedNewsModel>> = flow {
        feedRepository.getFeedNews {
            onSuccess()
        }.collect {
            emit(getAllFeed(checkForSubscribedChannels(it)))
        }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()

    private fun checkForSubscribedChannels(list: MutableList<ChannelModel>): MutableList<ChannelModel> {
        val resultList = mutableListOf<ChannelModel>()
        list.forEach { channel ->
            val subsList = ArrayList(channel.subscribers.values)
            run breaking@{
                subsList.forEach { sub ->
                    if (sub.uid == UID) {
                        resultList.add(channel)
                        return@breaking
                    }
                }
            }
        }
        return resultList
    }

    fun deleteNews(model: FeedNewsModel, onSuccess: () -> Unit) {
        feedRepository.deleteNews(model) {
            onSuccess()
        }
    }

    private fun getAllFeed(list: MutableList<ChannelModel>): MutableList<FeedNewsModel> {
        val resultList = mutableListOf<FeedNewsModel>()
        resultList.clear()
        list.forEach { channel ->
            val feedChannel = ArrayList(channel.feed.values)
            val feedNewsChannel = mutableListOf<FeedNewsModel>()
            feedChannel.forEach {
                var newsModel = FeedNewsModel(
                    adminId = it.adminId,
                    feedId = it.feedId,
                    iconUri = it.iconUri,
                    fileUri = it.fileUri,
                    text = it.text,
                    time = it.time,
                    nameFile = it.nameFile,
                    nameAdmin = it.nameAdmin,
                    heightImage = it.heightImage,
                    sizeFile = it.sizeFile,
                    channelName = channel.name,
                    channelId = channel.channelId,
                    channelIcon = channel.iconUrl
                )
                feedNewsChannel.add(newsModel)
            }
            resultList.addAll(feedNewsChannel)
        }
        resultList.sortBy { model ->
            model.time.toString()
        }
        return resultList
    }

    fun getChannelModel(channelId : String, onSuccess: (model : ChannelModel) -> Unit){
        feedRepository.getChannelModel(channelId){
            onSuccess(it)
        }
    }

    fun downloadFeedFile(path: String, text: String, function: () -> Unit) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            text
        )
        try {
            if (AppPermissions.checkPermission(WRITE_FILES)) {
                file.createNewFile()
                fileStorageRepository.getFileFromStorage(file, path) {
                    function()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG_FILE_STORAGE, "Download file error: " + e.message.toString())
        }
    }

    fun getForNewNotifications(onSuccess: (newNotifications : Boolean) -> Unit){
        feedRepository.getForNewNotifications {
            onSuccess(it)
        }
    }

    fun watchNotifications(){
        val notificationMainModel = mapOf<String, Boolean>(
            WRITTEN to true
        )
        feedRepository.watchNotifications(notificationMainModel)
    }
}