package com.example.alfamessanger.presentation.fragments.verification

import androidx.lifecycle.ViewModel
import com.example.alfamessanger.domain.usecases.GetCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerificationFragmentViewModel @Inject constructor(private val getCodeUseCase: GetCodeUseCase) : ViewModel() {

    fun enterCode(id:String, phone:String, code : String, onSuccess:() -> Unit){
        getCodeUseCase.enterCode(id, phone, code){
            onSuccess()
        }
    }
}