package com.example.alfamessanger.data.firebase

import android.util.Log
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedNewsModel
import com.example.alfamessanger.domain.models.NotificationsMainModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.FeedRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FeedRequests : FeedRepository {

    private var mListChannels = mutableListOf<ChannelModel>()

    override fun getFeedNews(onSuccess: () -> Unit): Flow<MutableList<ChannelModel>> =
        callbackFlow {
            DATABASE_REFERENCE.child(NODE_CHANNELS)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.exists()) {
                        mListChannels.clear()
                        for (child in it.children) {
                            mListChannels.add(
                                child.getValue(ChannelModel::class.java) ?: ChannelModel()
                            )
                        }
                    }
                    launch {
                        send(mListChannels)
                    }
                })
            awaitClose()
        }.distinctUntilChanged()

    override fun getChannelModel(channelId: String, onSuccess: (model: ChannelModel) -> Unit) {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelId)
            .addListenerForSingleValueEvent(AppValueEventListener {
                if (it.exists()) {
                    val model: ChannelModel =
                        it.getValue(ChannelModel::class.java) ?: ChannelModel()
                    onSuccess(model)
                }
            })
    }

    override fun deleteNews(model: FeedNewsModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(model.channelId).child(CHILD_FEED)
            .child(model.feedId).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_DELETE_FEED, it.message.toString())
            }
    }

    override fun getForNewNotifications(onSuccess: (newNotifications: Boolean) -> Unit) {
        DATABASE_REFERENCE.child(NODE_NOTIFICATIONS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                if (it.exists()) {
                    val model = it.getValue(NotificationsMainModel::class.java) ?: NotificationsMainModel()
                    onSuccess(model.written)
                }
            })
    }

    override fun watchNotifications(map: Map<String, Boolean>) {
        DATABASE_REFERENCE.child(NODE_NOTIFICATIONS).child(UID).updateChildren(map).addOnFailureListener {
            Log.e(TAG_NOTIFICATION, it.message.toString())
        }
    }
}