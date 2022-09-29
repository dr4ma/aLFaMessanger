package com.example.alfamessanger.domain.usecases

import android.os.Bundle
import android.util.Log
import com.example.alfamessanger.R
import com.example.alfamessanger.utills.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AuthUseCase {

    private lateinit var callbackAuth: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    fun authUser(phone: String) {

        callbackAuth = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        APP_ACTIVITY_REGISTER.mNavController.navigate(R.id.action_logInFragment_to_mainFragment)
                    }
                    else{
                        Log.e(TAG_REGISTER, "Verification error: " + result.exception.toString())
                    }
                }
            }

            override fun onVerificationFailed(error: FirebaseException) {
                showToast(error.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                val bundle = Bundle()
                bundle.putString(ID_AUTH_VERIFY, id)
                bundle.putString(PHONE_AUTH, phone)
                APP_ACTIVITY_REGISTER.mNavController.navigate(R.id.action_logInFragment_to_verificationFragment, bundle)
            }
        }

        val options = PhoneAuthOptions.newBuilder(AUTH)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(APP_ACTIVITY_REGISTER)                 // Activity (for callback binding)
            .setCallbacks(callbackAuth)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}