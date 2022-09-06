package com.example.alfamessanger.domain.usecases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.BlackListRepository
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import javax.inject.Inject

class BlackListUseCase @Inject constructor(private val blackListRepository: BlackListRepository) {

    val blackList: MutableLiveData<MutableList<BlackListUserModel>> = MutableLiveData()
    private lateinit var mObserverBlack: Observer<MutableList<BlackListUserModel>>

    fun getBlackList(function:() -> Unit){
        mObserverBlack = Observer {
            blackList.value = it
        }
        blackListRepository.blackList.observe(APP_ACTIVITY_MAIN, mObserverBlack)
        blackListRepository.getBlackList(){
            function()
        }
    }

    fun removeFromBlackList(model: BlackListUserModel, onSuccess : () -> Unit){
        blackListRepository.removeFromBlackList(model){
            onSuccess()
        }
    }

    fun removeObserver(){
        blackListRepository.blackList.removeObserver(mObserverBlack)
        blackListRepository.removeListener()
    }
}