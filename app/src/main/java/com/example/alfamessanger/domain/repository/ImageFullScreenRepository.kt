package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.SavedPhotoModel

interface ImageFullScreenRepository {

    fun saveImage(map : Map<String, String>, url : String, onSuccess :() -> Unit)
    fun deleteFromSaved(model : SavedPhotoModel, onSuccess: () -> Unit)
    fun getSavedPhotos(onSuccess:(list : MutableList<SavedPhotoModel>) -> Unit)
}