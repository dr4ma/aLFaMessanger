package com.example.alfamessanger.data.firebase

import android.util.Log
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.CreateChannelRepository
import com.example.alfamessanger.utills.*

class CreateChannelRequests : CreateChannelRepository {
    override fun createChannelId(onSuccess: (channelKey: String) -> Unit) {
        val key = DATABASE_REFERENCE.child(NODE_CHANNELS).push().key.toString()
        onSuccess(key)
    }

    override fun createChannel(
        channelId: String,
        model: ChannelModel,
        adminModel: UserModel,
        onSuccess: (model: ChannelModel) -> Unit
    ) {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelId).setValue(model)
            .addOnSuccessListener {
                onSuccess(model)
                createAdminAsSubscriber(channelId, adminModel)
            }
            .addOnFailureListener {
                Log.e(TAG_CREATE_CHANNEL, it.message.toString())
            }
    }

    private fun createAdminAsSubscriber(channelId: String, adminModel: UserModel) {
        DATABASE_REFERENCE.child(NODE_CHANNELS).child(channelId).child(CHILD_SUBSCRIBERS)
            .child(adminModel.uid.toString()).setValue(adminModel)
            .addOnFailureListener {
                Log.e(TAG_CREATE_CHANNEL, it.message.toString())
            }
    }


}