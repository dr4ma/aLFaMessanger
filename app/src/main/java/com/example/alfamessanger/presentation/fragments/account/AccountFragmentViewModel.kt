package com.example.alfamessanger.presentation.fragments.account

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.usecases.GetUserModelUseCase
import com.example.alfamessanger.domain.usecases.LoadPicUseCase
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountFragmentViewModel @Inject constructor(
    private val getUserModelUseCase: GetUserModelUseCase,
    private val loadPicUseCase: LoadPicUseCase
) : ViewModel() {

    val resultLoadUserdata = MutableLiveData<Boolean>()

    fun getUserModel() {
        resultLoadUserdata.value = false
        viewModelScope.launch {
            getUserModelUseCase.getUserModel() {
                resultLoadUserdata.value = true
            }
        }
    }

    fun loadPic(uri: Uri, path: StorageReference, function: (url: String) -> Unit) {
        loadPicUseCase.loadPic(uri, path) {
            function(it)
        }
    }
}