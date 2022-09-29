package com.example.alfamessanger.domain.usecases

import com.example.alfamessanger.domain.repository.RegisterRepository
import javax.inject.Inject

class GetCodeUseCase @Inject constructor(private val registerRepository: RegisterRepository) {

    fun enterCode(id: String, phone: String, code: String, onSuccess:() -> Unit) {

        registerRepository.signInWithCredential(phone, id, code){
            onSuccess()
        }

    }
}