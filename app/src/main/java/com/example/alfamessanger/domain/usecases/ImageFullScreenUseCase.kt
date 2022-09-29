package com.example.alfamessanger.domain.usecases

import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.domain.repository.ImageFullScreenRepository
import com.example.alfamessanger.domain.repository.SavedRepository
import com.example.alfamessanger.domain.repository.SingleChatRepository
import com.example.alfamessanger.utills.CHILD_ID
import com.example.alfamessanger.utills.CHILD_IMAGE_URL
import com.example.alfamessanger.utills.UID
import javax.inject.Inject

class ImageFullScreenUseCase @Inject constructor(
    private val imageFullScreenRepository: ImageFullScreenRepository,
    private val singleChatRepository: SingleChatRepository,
    private val savedRepository: SavedRepository
) {

    private var listPhotos = mutableListOf<SavedPhotoModel>()

    suspend fun saveImage(url: String, onSuccess: () -> Unit) {
        singleChatRepository.createMessageKey(UID){
            val map = mapOf(
                CHILD_ID to it,
                CHILD_IMAGE_URL to url
            )
            imageFullScreenRepository.saveImage(map, it) {
                onSuccess()
            }
        }
    }

    fun checkSavedOrNot(url: String, function: (saved : Boolean) -> Unit){
        imageFullScreenRepository.getSavedPhotos(){
            var flag = false
            listPhotos = it
            run breaking@{
                for (savedPhotoModel in it) {
                    if (savedPhotoModel.image_url == url) {
                        flag = true
                        return@breaking
                    }
                    else{
                        flag = false
                    }
                }
            }
            function(flag)
        }
    }

    fun deleteFromSaved(url : String, onSuccess: () -> Unit){
        listPhotos.forEach {
            if(it.image_url == url){
                imageFullScreenRepository.deleteFromSaved(it){
                    onSuccess()
                }
            }
        }
    }
}