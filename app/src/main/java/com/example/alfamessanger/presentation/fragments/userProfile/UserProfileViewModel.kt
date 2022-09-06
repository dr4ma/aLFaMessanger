package com.example.alfamessanger.presentation.fragments.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.NotificationsRepository
import com.example.alfamessanger.domain.usecases.NotificationsUseCase
import com.example.alfamessanger.domain.usecases.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val notificationsUseCase: NotificationsUseCase
) : ViewModel() {

    fun addInFriends(friendModel: UserModel, onSuccess: () -> Unit) {
        userProfileUseCase.addInFriends(friendModel) {
            onSuccess()
        }
    }

    fun checkFriendOrNot(friendModel: UserModel, onSuccess: (result: Boolean) -> Unit) {
        userProfileUseCase.checkFriendOrNot(friendModel) {
            onSuccess(it)
        }
    }

    fun checkBlockOrNot(model: UserModel, onSuccess: (result: Boolean) -> Unit) {
        userProfileUseCase.checkBlockOrNot(model) {
            onSuccess(it)
        }
    }

    fun deleteFromBlock(model: UserModel, onSuccess: () -> Unit) {
        userProfileUseCase.deleteFromBlock(model) {
            onSuccess()
        }
    }

    fun deleteFromFriends(friendModel: UserModel, onSuccess: () -> Unit) {
        userProfileUseCase.deleteFromFriends(friendModel) {
            onSuccess()
        }
    }

    fun deleteMeFromFriends(friendModel: UserModel) {
        userProfileUseCase.deleteMeFromFriends(friendModel)
    }

    fun checkForBlockMe(model: UserModel, function: (blocked: Boolean) -> Unit) {
        userProfileUseCase.checkForBlock(model) {
            function(it)
        }
    }

    fun addInBlock(text: String, userModel: UserModel, onSuccess: () -> Unit) {
        userProfileUseCase.addInBlock(text, userModel) {
            onSuccess()
        }
    }

    fun sendNotification(userModel: UserModel, tittle: String, message: String) {
        viewModelScope.launch {
            notificationsUseCase.sendNotification(userModel, tittle, message)
        }
    }
}