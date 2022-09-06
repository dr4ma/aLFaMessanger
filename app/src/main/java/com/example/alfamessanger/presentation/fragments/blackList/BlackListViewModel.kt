package com.example.alfamessanger.presentation.fragments.blackList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.usecases.BlackListUseCase
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BlackListViewModel @Inject constructor(private val blackListUseCase: BlackListUseCase) : ViewModel() {

    val blackList: MutableLiveData<MutableList<BlackListUserModel>> = MutableLiveData()
    private lateinit var mObserverBlack: Observer<MutableList<BlackListUserModel>>

    fun getBlackList(function:() -> Unit){
        mObserverBlack = Observer {
            blackList.value = it
        }
        blackListUseCase.blackList.observe(APP_ACTIVITY_MAIN, mObserverBlack)
        blackListUseCase.getBlackList(){
            function()
        }
    }

    fun removeFromBlackList(model : BlackListUserModel, onSuccess : () -> Unit){
        blackListUseCase.removeFromBlackList(model){
            onSuccess()
        }
    }

    fun removeObserver(){
        blackListUseCase.blackList.removeObserver(mObserverBlack)
    }
}