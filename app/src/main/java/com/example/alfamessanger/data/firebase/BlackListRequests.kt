package com.example.alfamessanger.data.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.repository.BlackListRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BlackListRequests : BlackListRepository {

    private var mListBlack = mutableListOf<BlackListUserModel>()

    override fun getBlackList(function: () -> Unit) : Flow<MutableList<BlackListUserModel>> = callbackFlow {
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID).addListenerForSingleValueEvent(
            AppValueEventListener {
                if (it.exists()) {
                    mListBlack.clear()
                    for (child in it.children) {
                        mListBlack.add(
                            child.getValue(BlackListUserModel::class.java) ?: BlackListUserModel()
                        )
                    }
                }
                launch {
                    send(mListBlack)
                }
            })

        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                if (!it.exists()) {
                    function()
                }
            })
        awaitClose()
    }.distinctUntilChanged()

    override fun removeFromBlackList(model: BlackListUserModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(UID).child(model.uid.toString())
            .removeValue().addOnFailureListener {
            Log.e(TAG_BLACK_LIST, "remove from black list error: " + it.message.toString())
        }
            .addOnSuccessListener {
                onSuccess()
            }
    }
}