package com.example.alfamessanger.presentation.fragments.changeUserData

import androidx.lifecycle.ViewModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.GetUserModelUseCase
import com.example.alfamessanger.utills.CHILD_BIO
import com.example.alfamessanger.utills.CHILD_NAME
import com.example.alfamessanger.utills.CHILD_NICKNAME
import com.example.alfamessanger.utills.USER
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeUserDataFragmentViewModel @Inject constructor(private val getUserModelUseCase: GetUserModelUseCase) :
    ViewModel() {

    private var checkNick = false


    fun saveNewUserData(bio: String, nickname: String, name: String, onSuccess: () -> Unit) {
        val mapOfData = mapOf(
            CHILD_BIO to bio,
            CHILD_NICKNAME to nickname,
            CHILD_NAME to name
        )

        getUserModelUseCase.saveNewUserData(mapNewData = mapOfData) {
            onSuccess()
        }
    }

    fun checkNickName(nickName: String, list: MutableList<UserModel>): Boolean {
        checkNick = false
        run breaking@{
            list.forEach {
                if (it.nickname != nickName) {
                    checkNick = true
                } else {
                    if(it.uid != USER.uid){
                        checkNick = false
                        return@breaking
                    }
                    else{
                        checkNick = true
                        return@breaking
                    }
                }
            }
        }
        return checkNick
    }
}