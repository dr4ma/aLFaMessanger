package com.example.alfamessanger.presentation.fragments.imageFullScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.usecases.ImageFullScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImageFullScreenViewModel @Inject constructor(private val imageFullScreenUseCase: ImageFullScreenUseCase) : ViewModel() {

    fun saveImage(url : String, onSuccess :() -> Unit){
        viewModelScope.launch {
            imageFullScreenUseCase.saveImage(url) {
                onSuccess()
            }
        }
    }

    fun checkSavedOrNot(url: String, function: (saved : Boolean) -> Unit){
        imageFullScreenUseCase.checkSavedOrNot(url){
            function(it)
        }
    }
    fun deleteFromSaved(url: String, function: () -> Unit){
        imageFullScreenUseCase.deleteFromSaved(url){
            function()
        }
    }
}