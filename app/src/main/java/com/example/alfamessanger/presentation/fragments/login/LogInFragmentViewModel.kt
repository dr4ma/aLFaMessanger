package com.example.alfamessanger.presentation.fragments.login

import androidx.lifecycle.ViewModel
import com.example.alfamessanger.domain.usecases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInFragmentViewModel @Inject constructor(private val authUseCase : AuthUseCase) : ViewModel() {

    fun authUser(phone : String){
        authUseCase.authUser(phone)
    }
}