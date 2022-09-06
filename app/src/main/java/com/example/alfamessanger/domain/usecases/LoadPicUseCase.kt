package com.example.alfamessanger.domain.usecases

import android.graphics.Bitmap
import android.net.Uri
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.FileStorageRepository
import com.example.alfamessanger.domain.repository.SingleChatRepository
import com.example.alfamessanger.utills.TYPE_IMAGE
import com.example.alfamessanger.utills.TYPE_VOICE
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class LoadPicUseCase @Inject constructor(
    private val loadFileRepository: FileStorageRepository,
    private val singleChatRepository: SingleChatRepository
) {

    fun loadPic(uri: Uri, path: StorageReference, function: (url: String) -> Unit) {
        loadFileRepository.putFileToStorage(uri, path) {
            loadFileRepository.getUrlFromStorage(path) { path ->
                loadFileRepository.putUrlToDatabase(path) { url ->
                    function(url)
                }
            }
        }
    }

    fun loadPicMessage(model : UserModel, messageKey : String, receivingId : String, uri: Uri, path: StorageReference, bitmapImage : Bitmap, onSuccess:() -> Unit) {
        loadFileRepository.putFileToStorage(uri, path) {
            loadFileRepository.getUrlFromStorage(path) { path ->
                singleChatRepository.sendImageMessage(receivingId, path, messageKey, bitmapImage){
                    val time =  ServerValue.TIMESTAMP
                    singleChatRepository.createChat(model, TYPE_IMAGE, "", model.uid.toString(), time)
                    onSuccess()
                }
            }
        }
    }
}