package com.example.alfamessanger.presentation.fragments.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.domain.usecases.SavedUseCase
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedFragmentViewModel @Inject constructor(private val savedUseCase: SavedUseCase) : ViewModel() {

    private lateinit var mObserverPhotos: Observer<MutableList<SavedPhotoModel>>
    val photos: MutableLiveData<MutableList<SavedPhotoModel>> = MutableLiveData()

    fun getPhotos(function:() -> Unit){
        mObserverPhotos = Observer {
            photos.value = it
        }
        savedUseCase.photos.observe(APP_ACTIVITY_MAIN, mObserverPhotos)
        savedUseCase.getPhotos(){
            function()
        }
    }
}