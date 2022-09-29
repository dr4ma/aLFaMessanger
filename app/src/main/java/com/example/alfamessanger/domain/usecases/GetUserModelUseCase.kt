package com.example.alfamessanger.domain.usecases

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.GetUserModelRepository
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.TAG
import javax.inject.Inject

class GetUserModelUseCase @Inject constructor(private val getUserModelRepository: GetUserModelRepository) {

    val listAllUsers : MutableLiveData<MutableList<UserModel>> = MutableLiveData<MutableList<UserModel>>()
    private lateinit var mObserverLoadAllUsers: Observer<MutableList<UserModel>>

    suspend fun getUserModel(onSuccess: () -> Unit) {
        getUserModelRepository.getUserModel() {
            onSuccess()
        }
    }

    fun saveNewUserData(mapNewData: Map<String, String>, onSuccess: () -> Unit) {
        getUserModelRepository.saveNewUserData(mapNewData) {
            onSuccess()
        }
    }

    fun getAllUsers() {
        mObserverLoadAllUsers = Observer {
            listAllUsers.value = it
        }
        getUserModelRepository.listAllUsersResult.observe(APP_ACTIVITY_MAIN, mObserverLoadAllUsers)
        getUserModelRepository.getAllUsers {
            Log.i(TAG, "Get all users is success")
        }
    }

    fun setPrivacyAccount(privacy : Boolean){
        val map = mapOf(
            "privateAccount" to privacy
        )
        getUserModelRepository.setPrivacyAccount(map)
    }

    fun removeObservers(){
        getUserModelRepository.listAllUsersResult.removeObserver(mObserverLoadAllUsers)
        getUserModelRepository.deleteListeners()
    }
}