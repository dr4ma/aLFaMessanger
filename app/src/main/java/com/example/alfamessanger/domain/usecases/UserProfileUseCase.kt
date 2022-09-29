package com.example.alfamessanger.domain.usecases

import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.SingleChatRepository
import com.example.alfamessanger.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val singleChatRepository: SingleChatRepository
) {

    fun addInFriends(friendModel: UserModel, onSuccess: () -> Unit) {
        userProfileRepository.addInFriends(friendModel) {
            onSuccess()
        }
    }

    fun checkFriendOrNot(friendModel: UserModel, onSuccess: (result: Boolean) -> Unit) {
        userProfileRepository.checkFriendOrNot(friendModel) {
            onSuccess(it)
        }
    }

    fun checkBlockOrNot(model: UserModel, onSuccess: (result: Boolean) -> Unit) {
        userProfileRepository.checkBlockOrNot(model) {
            onSuccess(it)
        }
    }

    fun deleteFromBlock(model: UserModel, onSuccess: () -> Unit) {
        userProfileRepository.deleteFromBlock(model) {
            onSuccess()
        }
    }


    fun deleteFromFriends(friendModel: UserModel, onSuccess: () -> Unit) {
        userProfileRepository.deleteFromFriends(friendModel) {
            onSuccess()
        }
    }

    fun deleteMeFromFriends(friendModel: UserModel) {
        userProfileRepository.deleteMeFromFriends(friendModel)
    }

    fun checkForBlock(model: UserModel, function: (blocked : Boolean) -> Unit){
        singleChatRepository.checkForBlock(model){
            function(it)
        }
    }

    fun addInBlock(text: String, userModel: UserModel, onSuccess: () -> Unit) {
        val userModelBlack = BlackListUserModel(
            uid = userModel.uid,
            phone = userModel.phone,
            name = userModel.name,
            nickname = userModel.nickname,
            bio = userModel.bio,
            status = userModel.status,
            iconUrl = userModel.iconUrl,
            tittle = text
        )
        userProfileRepository.addInBlock(userModelBlack) {
            onSuccess()
        }
    }
}