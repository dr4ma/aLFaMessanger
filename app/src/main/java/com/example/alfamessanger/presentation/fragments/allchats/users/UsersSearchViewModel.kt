package com.example.alfamessanger.presentation.fragments.allchats.users

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.SearchHistoryUserModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.GetUserModelUseCase
import com.example.alfamessanger.domain.usecases.SearchUsersHistoryUseCase
import com.example.alfamessanger.utills.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersSearchViewModel @Inject constructor(
    private val getUserModelUseCase: GetUserModelUseCase,
    private val searchUsersHistoryUseCase: SearchUsersHistoryUseCase
) : ViewModel() {

    val listUsers: MutableLiveData<MutableList<UserModel>> =
        MutableLiveData<MutableList<UserModel>>()

    val searchList = mutableListOf<UserModel>()

    val listHistory : MutableLiveData<MutableList<SearchHistoryUserModel>> =
        MutableLiveData<MutableList<SearchHistoryUserModel>>()

    private lateinit var mObserverLoadAllUsers: Observer<MutableList<UserModel>>
    private lateinit var mObserverHistory: Observer<MutableList<SearchHistoryUserModel>>

    fun searchUser(nickName: String, function: (MutableList<UserModel>) -> Unit) {
        searchList.clear()
        listUsers.value?.forEach { user ->
            if (user.nickname.contains(nickName)) {
                if (user.nickname == "@$nickName") {
                    searchList.add(0, user)
                } else {
                    searchList.add(user)
                }
            }
        }
        function(searchList)
    }

    fun getAllUsers() {
        mObserverLoadAllUsers = Observer {
            listUsers.value = it
        }
        getUserModelUseCase.listAllUsers.observe(APP_ACTIVITY_MAIN, mObserverLoadAllUsers)
        getUserModelUseCase.getAllUsers()

    }

    fun getHistoryUser() {
       mObserverHistory = Observer {
           listHistory.value = it.asReversed()
       }
        searchUsersHistoryUseCase.historyList.observe(APP_ACTIVITY_MAIN, mObserverHistory)
        searchUsersHistoryUseCase.getHistory()
    }

    fun insertDataInHistory(historyModel: UserModel) {
      searchUsersHistoryUseCase.insertUserInHistory(historyModel)
    }

    fun removeObservers() {
        getUserModelUseCase.listAllUsers.removeObserver(mObserverLoadAllUsers)
        getUserModelUseCase.removeObservers()
    }
}