package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.UserModel

interface RegisterRepository {

    fun checkUserForRegister(newUser : UserModel, onSuccess:() -> Unit)
    fun createNewUser(newUser : UserModel, onSuccess: () -> Unit)
    fun signInWithCredential(phone: String, id: String, code: String, onSuccess:() -> Unit)
}