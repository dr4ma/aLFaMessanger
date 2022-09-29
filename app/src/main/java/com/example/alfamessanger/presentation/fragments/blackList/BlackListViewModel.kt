package com.example.alfamessanger.presentation.fragments.blackList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.usecases.BlackListUseCase
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlackListViewModel @Inject constructor(private val blackListUseCase: BlackListUseCase) : ViewModel() {

    val blackList: MutableLiveData<MutableList<BlackListUserModel>> = MutableLiveData()

    fun getBlackList(function:() -> Unit){
        viewModelScope.launch {
            blackListUseCase.getBlackList(){
                function()
            }.collect {
                blackList.postValue(it)
            }
        }
    }

    fun removeFromBlackList(model : BlackListUserModel, onSuccess : () -> Unit){
        blackListUseCase.removeFromBlackList(model){
            onSuccess()
        }
    }
}