package com.example.alfamessanger.presentation.fragments.friendsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.FriendsListUseCase
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsListViewModel @Inject constructor(private val friendsListUseCase: FriendsListUseCase) : ViewModel() {

    private lateinit var mObserverFriends: Observer<MutableList<UserModel>>
    val friend: MutableLiveData<MutableList<UserModel>> = MutableLiveData()

    fun getFriends(function:() -> Unit){
        viewModelScope.launch {
            mObserverFriends = Observer {
                friend.value = it
            }
            friendsListUseCase.friend.observe(APP_ACTIVITY_MAIN, mObserverFriends)
            friendsListUseCase.getFriends(){
                function()
            }
        }
    }

    fun removeObservers(){
        friendsListUseCase.friend.removeObserver(mObserverFriends)
        friendsListUseCase.removeObservers()
    }
}