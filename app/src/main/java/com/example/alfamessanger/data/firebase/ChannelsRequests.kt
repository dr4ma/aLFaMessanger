package com.example.alfamessanger.data.firebase

import android.util.Log
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.*
import com.example.alfamessanger.domain.repository.ChannelRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ChannelsRequests : ChannelRepository {

    private lateinit var mListenerGetChannels: ChildEventListener
    override val feed: MutableLiveData<FeedModel> = MutableLiveData()
    private lateinit var channelIdStr: String
    private var mListSubs = mutableListOf<UserModel>()

    override fun createFeedId(onSuccess: (channelKey: String) -> Unit) {
        val key = DATABASE_REFERENCE.child(NODE_CHANNELS).push().key.toString()
        onSuccess(key)
    }

    override fun createFeed(
        channelId: String,
        model: FeedModel,
        onSuccess: (model: FeedModel) -> Unit
    ) {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelId).child(CHILD_FEED)
            .child(model.feedId)
            .setValue(model)
            .addOnSuccessListener {
                onSuccess(model)
            }
            .addOnFailureListener {
                Log.e(TAG_CREATE_FEED, it.message.toString())
            }
    }

    override fun getMessages(channelId: String, function: () -> Unit) {
        channelIdStr = channelId
        mListenerGetChannels = object : ChildEventListener {
            override fun onChildAdded(item: DataSnapshot, previousChildName: String?) {
                if (item.exists()) {
                    feed.value = item.getValue(FeedModel::class.java)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelId).child(CHILD_FEED)
            .removeEventListener(mListenerGetChannels)
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelId).child(CHILD_FEED)
            .addChildEventListener(mListenerGetChannels)
    }

    override fun getSubs(channelId: String, onSuccess: (MutableList<UserModel>) -> Unit){
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
                onSuccess(mListSubs)

            })
    }

    override fun subscribeChannel(
        channelModel: ChannelModel,
        userModel: UserModel,
        function: () -> Unit
    ) {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelModel.channelId).child(CHILD_SUBSCRIBERS)
            .child(userModel.uid.toString()).setValue(userModel)
            .addOnSuccessListener {
                plusCountSubs(channelModel){
                    function()
                }
            }
            .addOnFailureListener {
                Log.e(TAG_CHANNEL, it.message.toString())
            }
    }

    override fun unSubscribeChannel(
        channelModel: ChannelModel,
        userModel: UserModel,
        function: () -> Unit
    ) {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelModel.channelId).child(CHILD_SUBSCRIBERS)
            .child(userModel.uid.toString()).removeValue()
            .addOnSuccessListener {
                minusCountSubs(channelModel){
                    function()
                }
            }
            .addOnFailureListener {
                Log.e(TAG_CHANNEL, it.message.toString())
            }
    }

    private fun plusCountSubs(channelModel: ChannelModel, function:() -> Unit) {
        val mapCount: Map<String, Int> = mapOf(
            CHILD_COUNT_SUBS to channelModel.countSubs + 1
        )
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelModel.channelId).updateChildren(mapCount).addOnSuccessListener {
            function()
        }
    }

    private fun minusCountSubs(channelModel: ChannelModel, function:() -> Unit) {
        val mapCount: Map<String, Int> = mapOf(
            CHILD_COUNT_SUBS to channelModel.countSubs - 1
        )
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelModel.channelId).updateChildren(mapCount).addOnSuccessListener {
            function()
        }
    }


    override fun deleteListener() {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelIdStr).child(CHILD_FEED)
            .removeEventListener(mListenerGetChannels)
        feed.value = FeedModel()
    }

    override fun deleteNews(model: FeedModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(model.channelId).child(CHILD_FEED).child(model.feedId).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_DELETE_FEED, it.message.toString())
            }
    }

    override fun deleteChannel(channelId: String, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelId).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_DELETE_FEED, it.message.toString())
            }
    }
}