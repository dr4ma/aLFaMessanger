package com.example.alfamessanger.domain.usecases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.FriendsListRepository
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import javax.inject.Inject

class FriendsListUseCase @Inject constructor(private val friendsListRepository: FriendsListRepository) {

    private lateinit var mObserverFriends: Observer<MutableList<UserModel>>
    val friend: MutableLiveData<MutableList<UserModel>> = MutableLiveData()

    suspend fun getFriends(function: () -> Unit) {
        mObserverFriends = Observer {
            friend.value = it
        }
        friendsListRepository.friendsList.observe(APP_ACTIVITY_MAIN, mObserverFriends)
        friendsListRepository.getFriends() {
            function()
        }
    }

    fun removeObservers() {
        friendsListRepository.friendsList.removeObserver(mObserverFriends)
    }
}