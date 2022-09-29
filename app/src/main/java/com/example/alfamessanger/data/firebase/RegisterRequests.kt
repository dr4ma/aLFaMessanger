package com.example.alfamessanger.data.firebase

import android.util.Log
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.RegisterRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class RegisterRequests : RegisterRepository {

    override fun signInWithCredential(
        phone: String,
        id: String,
        code: String,
        onSuccess: () -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var token = ""
                FirebaseMessaging.getInstance().token.addOnSuccessListener {
                    token = it
                }
                UID = AUTH.currentUser?.uid!!
                val newUser = UserModel(
                    phone = phone,
                    uid = UID,
                    name = UID,
                    status = DEFAULT_STATUS,
                    bio = "-",
                    privateAccount = false,
                    nickname = "@"+UID.lowercase(
                        Locale.getDefault()
                    ),
                    tokenNotification = token
                )

                checkUserForRegister(newUser) {
                    onSuccess()
                }
            } else {
                Log.e(TAG_REGISTER, "Register error: " + task.exception.toString())
            }
        }
    }

    override fun checkUserForRegister(newUser: UserModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_USERS).addListenerForSingleValueEvent(AppValueEventListener {
            if (it.hasChild(UID)) {
                onSuccess()
            } else {
                createNewUser(newUser) {
                    onSuccess()
                }
            }
        })
    }

    override fun createNewUser(newUser: UserModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_USERS).child(UID).setValue(newUser).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            Log.e(TAG_REGISTER, "New user error: " + it.message.toString())
        }
    }
}