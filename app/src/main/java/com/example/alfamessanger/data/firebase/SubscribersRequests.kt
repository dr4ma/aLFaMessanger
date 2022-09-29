package com.example.alfamessanger.data.firebase

import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.SubscribersRepository
import com.example.alfamessanger.utills.CHILD_SUBSCRIBERS
import com.example.alfamessanger.utills.DATABASE_REFERENCE
import com.example.alfamessanger.utills.NODE_CHANNELS
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SubscribersRequests : SubscribersRepository {

    private var mListSubs = mutableListOf<UserModel>()

    override fun getSubs(channelId: String): Flow<MutableList<UserModel>> = callbackFlow {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelId)
            .child(CHILD_SUBSCRIBERS)
            .addListenerForSingleValueEvent(AppValueEventListener {
                if (it.exists()) {
                    mListSubs.clear()
                    for (child in it.children) {
                        mListSubs.add(
                            child.getValue(UserModel::class.java) ?: UserModel()
                        )
                    }
                }
                launch {
                    send(mListSubs)
                }
            })
        awaitClose()
    }.distinctUntilChanged()
}