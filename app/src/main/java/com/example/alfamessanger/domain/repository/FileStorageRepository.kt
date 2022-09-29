package com.example.alfamessanger.domain.repository

import android.net.Uri
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedModel
import com.google.firebase.storage.StorageReference
import java.io.File

interface FileStorageRepository {
    fun putFileToStorage(uri: Uri, path: StorageReference, function: () -> Unit)
    fun getUrlFromStorage(path: StorageReference, function: (url: String) -> Unit)
    fun putUrlToDatabase(url: String, function: (url: String) -> Unit)
    fun getFileFromStorage(file: File, fileUrl: String, function: () -> Unit)
    fun removeFileFromStorage(fileUrl: String)
    fun putIconUrlToChannel(model: ChannelModel, url: String)
    fun putImageFeed(channelId : String, model: FeedModel, url: String)
    fun putFileFeed(channelId : String, model: FeedModel, url: String)
}