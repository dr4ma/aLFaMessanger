package com.example.alfamessanger.domain.repository

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.domain.models.UserModel

interface SavedRepository {

    val photosList : MutableLiveData<MutableList<SavedPhotoModel>>
        get() = MutableLiveData()
    fun getSavedPhotos(function:() -> Unit)
}