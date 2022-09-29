package com.example.alfamessanger.data.firebase

import android.util.Log
import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.UserProfileRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener

class UserProfileRequests : UserProfileRepository {

    private var resultCheckFriend = false
    private var resultCheckBlock = false

    override fun addInFriends(friendModel: UserModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_FRIENDS).child(UID).child(friendModel.uid.toString()).setValue(friendModel)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_FRIENDS_LIST, "Add in friends error: " + it.message.toString())
            }
    }

    override fun deleteFromFriends(friendModel: UserModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_FRIENDS).child(UID).child(friendModel.uid.toString()).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_FRIENDS_LIST, "Delete from friends error: " + it.message.toString())
            }
    }

    override fun deleteMeFromFriends(friendModel: UserModel) {
        DATABASE_REFERENCE.child(NODE_FRIENDS).child(friendModel.uid.toString()).child(UID).removeValue()
            .addOnFailureListener {
                Log.e(TAG_FRIENDS_LIST, "Delete me from friends error: " + it.message.toString())
            }
    }

    override fun deleteFromBlock(model: UserModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID).child(model.uid.toString()).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_FRIENDS_LIST, "Delete from block error: " + it.message.toString())
            }
    }

    override fun checkFriendOrNot(friendModel: UserModel, onSuccess: (result: Boolean) -> Unit) {
        DATABASE_REFERENCE.child(NODE_FRIENDS).child(UID).child(friendModel.uid.toString()).addListenerForSingleValueEvent(
            AppValueEventListener{
            resultCheckFriend = it.exists()
            onSuccess(resultCheckFriend)
        })
    }

    override fun checkBlockOrNot(model: UserModel, onSuccess: (result: Boolean) -> Unit) {
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID).child(model.uid.toString()).addListenerForSingleValueEvent(
            AppValueEventListener{
            resultCheckBlock = it.exists()
            onSuccess(resultCheckBlock)
        })
    }

    override fun addInBlock(blockModel: BlackListUserModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID).child(blockModel.uid.toString()).setValue(blockModel)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_FRIENDS_LIST, "Add in block error: " + it.message.toString())
            }
    }
}