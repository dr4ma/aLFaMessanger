package com.example.alfamessanger.domain.usecases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.domain.repository.SavedRepository
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import javax.inject.Inject

class SavedUseCase @Inject constructor(private val savedRepository: SavedRepository) {

    private lateinit var mObserverPhotos: Observer<MutableList<SavedPhotoModel>>
    val photos: MutableLiveData<MutableList<SavedPhotoModel>> = MutableLiveData()

    fun getPhotos(function:() -> Unit){
        mObserverPhotos = Observer {
            photos.value = it
        }
        savedRepository.photosList.observe(APP_ACTIVITY_MAIN, mObserverPhotos)
        savedRepository.getSavedPhotos(){
            function()
        }
    }
}