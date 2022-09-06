package com.example.alfamessanger.data.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.repository.BlackListRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener

class BlackListRequests : BlackListRepository {

    private var mListBlack = mutableListOf<BlackListUserModel>()
    override val blackList: MutableLiveData<MutableList<BlackListUserModel>> = MutableLiveData()

    override fun getBlackList(function: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID).addListenerForSingleValueEvent(
            AppValueEventListener{
            if(it.exists()){
                mListBlack.clear()
                for(child in it.children){
                    mListBlack.add(child.getValue(BlackListUserModel::class.java) ?: BlackListUserModel())
                }
            }
            blackList.value = mListBlack
        })
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                if(!it.exists()){
                    function()
                }
            })
    }

    override fun removeFromBlackList(model: BlackListUserModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID).child(model.uid.toString()).removeValue().addOnFailureListener {
            Log.e(TAG_BLACK_LIST, "remove from black list error: " + it.message.toString())
        }
            .addOnSuccessListener {
                onSuccess()
            }
    }

    override fun removeListener() {
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID).removeEventListener(
            AppValueEventListener{})
    }
}