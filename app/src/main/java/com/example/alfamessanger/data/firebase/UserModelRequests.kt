package com.example.alfamessanger.data.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.GetUserModelRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener

class UserModelRequests : GetUserModelRepository {

    private lateinit var listenerAllUsers: AppValueEventListener
    private var listAllUsers: MutableList<UserModel> = mutableListOf()
    override val listAllUsersResult: MutableLiveData<MutableList<UserModel>> = MutableLiveData()

    override suspend fun getUserModel(onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener { data ->
                USER = data.getValue(UserModel::class.java) ?: UserModel()
                onSuccess()
            })
    }

    override fun getOtherUserModel(id: String, onSuccess: (model: UserModel) -> Unit) {
        DATABASE_REFERENCE.child(NODE_USERS).child(id)
            .addListenerForSingleValueEvent(AppValueEventListener { data ->
                val model = data.getValue(UserModel::class.java) ?: UserModel()
                onSuccess(model)
            })
    }

    override fun saveNewUserData(mapNewData: Map<String, String>, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_USERS).child(UID).updateChildren(mapNewData)
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                Log.e(TAG_USER_MODEL, "Change user data error: " + it.message)
            }
    }

    override fun getAllUsers(onSuccess: () -> Unit){
            listenerAllUsers = AppValueEventListener { data ->
                if (data.exists()) {
                    listAllUsers.clear()
                    for (user in data.children) {
                        listAllUsers.add(user.getValue(UserModel::class.java) ?: UserModel())
                    }
                    listAllUsersResult.value = listAllUsers
                }
            }
        DATABASE_REFERENCE.child(NODE_USERS).addValueEventListener(listenerAllUsers)
    }

    override fun setPrivacyAccount(map: Map<String, Boolean>) {
        DATABASE_REFERENCE.child(NODE_USERS).child(UID).updateChildren(map).addOnFailureListener {
            Log.e(TAG_USER_MODEL, it.message.toString())
        }
    }

    override fun deleteListeners() {
        DATABASE_REFERENCE.child(NODE_USERS).removeEventListener(listenerAllUsers)
    }
}