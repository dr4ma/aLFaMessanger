package com.example.alfamessanger.data.firebase

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.SingleChatRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue

class SingleChatRequests : SingleChatRepository {




    //TODO : ПЕРЕНЕСТИ ЛОГИКУ MAPS В USECASES




    private lateinit var mListenerGetMessages: ChildEventListener
    override val message: MutableLiveData<MessageModel> = MutableLiveData()
    override val messageDelete: MutableLiveData<MessageModel> = MutableLiveData()
    private var path = ""
    private lateinit var mapUid: Map<String, Any>
    private lateinit var mapReceiving: Map<String, Any>

    override suspend fun sendMessage(sendingMap: HashMap<String, Any>, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.updateChildren(sendingMap)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_SINGLE_CHAT, "Send message error: " + it.message.toString())
            }
    }

    override fun getMessages(pathUser: String, countMessages: Int, function: () -> Unit) {
        path = pathUser
        mListenerGetMessages = object : ChildEventListener {

            override fun onChildAdded(item: DataSnapshot, previousChildName: String?) {
                if (item.exists()) {
                    message.value = item.getValue(MessageModel::class.java)
                }
            }

            override fun onChildChanged(item: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(item: DataSnapshot) {
                if (item.exists()) {
                    messageDelete.value = item.getValue(MessageModel::class.java)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG_SINGLE_CHAT, "Get message error: " + error.message)
            }
        }
        DATABASE_REFERENCE.child(NODE_MESSAGES).child(UID).child(pathUser)
            .removeEventListener(mListenerGetMessages)
        DATABASE_REFERENCE.child(NODE_MESSAGES).child(UID).child(pathUser)
            .limitToLast(countMessages).addChildEventListener(mListenerGetMessages)
        DATABASE_REFERENCE.child(NODE_MESSAGES).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                if (!it.hasChild(pathUser)) {
                    function()
                }
            })
    }

    override fun createChat(
        userModel: UserModel,
        messageType: String,
        message: String,
        receivingId: String,
        time: Any
    ) {
        when (messageType) {
            TYPE_TEXT -> {
                mapUid = mapOf(
                    CHILD_TEXT to message,
                    CHILD_ICON_URL to userModel.iconUrl,
                    CHILD_TIMESTAMP to time,
                    CHILD_FROM to UID,
                    CHILD_ID to receivingId,
                    CHILD_NAME to userModel.name,
                    CHILD_NICKNAME to userModel.nickname
                )
                mapReceiving = mapOf(
                    CHILD_TEXT to message,
                    CHILD_ICON_URL to USER.iconUrl,
                    CHILD_TIMESTAMP to time,
                    CHILD_FROM to UID,
                    CHILD_ID to UID,
                    CHILD_NAME to USER.name,
                    CHILD_NICKNAME to USER.nickname
                )
            }
            TYPE_FILE -> {
                mapUid = mapOf(
                    CHILD_TEXT to "Файл",
                    CHILD_ICON_URL to userModel.iconUrl,
                    CHILD_TIMESTAMP to time,
                    CHILD_FROM to UID,
                    CHILD_ID to receivingId,
                    CHILD_NAME to userModel.name,
                    CHILD_NICKNAME to userModel.nickname
                )
                mapReceiving = mapOf(
                    CHILD_TEXT to "Файл",
                    CHILD_ICON_URL to USER.iconUrl,
                    CHILD_TIMESTAMP to time,
                    CHILD_FROM to UID,
                    CHILD_ID to UID,
                    CHILD_NAME to USER.name,
                    CHILD_NICKNAME to USER.nickname

                )
            }
            TYPE_VOICE -> {
                mapUid = mapOf(
                    CHILD_TEXT to "Голосовое сообщение",
                    CHILD_ICON_URL to userModel.iconUrl,
                    CHILD_TIMESTAMP to time,
                    CHILD_FROM to UID,
                    CHILD_ID to receivingId,
                    CHILD_NAME to userModel.name,
                    CHILD_NICKNAME to userModel.nickname
                )
                mapReceiving = mapOf(
                    CHILD_TEXT to "Голосовое сообщение",
                    CHILD_ICON_URL to USER.iconUrl,
                    CHILD_TIMESTAMP to time,
                    CHILD_FROM to UID,
                    CHILD_ID to UID,
                    CHILD_NAME to USER.name,
                    CHILD_NICKNAME to USER.nickname

                )
            }
            TYPE_IMAGE -> {
                mapUid = mapOf(
                    CHILD_TEXT to "Изображение",
                    CHILD_ICON_URL to userModel.iconUrl,
                    CHILD_TIMESTAMP to time,
                    CHILD_FROM to UID,
                    CHILD_ID to receivingId,
                    CHILD_NAME to userModel.name,
                    CHILD_NICKNAME to userModel.nickname
                )
                mapReceiving = mapOf(
                    CHILD_TEXT to "Изображение",
                    CHILD_ICON_URL to USER.iconUrl,
                    CHILD_TIMESTAMP to time,
                    CHILD_FROM to UID,
                    CHILD_ID to UID,
                    CHILD_NAME to USER.name,
                    CHILD_NICKNAME to USER.nickname

                )
            }
        }
        DATABASE_REFERENCE.child(NODE_CHATS).child(UID).child(receivingId).updateChildren(mapUid)
            .addOnFailureListener {
                Log.e(TAG_SINGLE_CHAT, "Edit message error: " + it.message.toString())
            }
        DATABASE_REFERENCE.child(NODE_CHATS).child(receivingId).child(UID)
            .updateChildren(mapReceiving)
            .addOnFailureListener {
                Log.e(TAG_SINGLE_CHAT, "Edit message error: " + it.message.toString())
            }
    }

    override fun sendImageMessage(
        receivingUserId: String,
        imageUrl: String,
        messageKey: String,
        bitmapImage: Bitmap,
        onSuccess: () -> Unit
    ) {

        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = UID
        mapMessage[CHILD_TYPE] = TYPE_IMAGE
        mapMessage[CHILD_ID] = messageKey
        mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
        mapMessage[CHILD_IMAGE_URL] = imageUrl
        mapMessage[CHILD_WIDTH] = bitmapImage.width
        mapMessage[CHILD_HEIGHT] = bitmapImage.height

        loadFileMessageToStorage(receivingUserId, messageKey, mapMessage)
        onSuccess()
    }

    override fun uploadVoiceMessage(
        receivingUserId: String,
        url: String,
        messageKey: String,
        duration: Int,
        onSuccess: () -> Unit
    ) {

        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = UID
        mapMessage[CHILD_TYPE] = TYPE_VOICE
        mapMessage[CHILD_ID] = messageKey
        mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
        mapMessage[CHILD_IMAGE_URL] = url
        mapMessage[CHILD_DURATION] = duration

        loadFileMessageToStorage(receivingUserId, messageKey, mapMessage)
        onSuccess()
    }

    override suspend fun sendFile(
        receivingUserId: String,
        url: String,
        messageKey: String,
        name: String,
        size: Double,
        onSuccess: () -> Unit
    ) {
        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = UID
        mapMessage[CHILD_TYPE] = TYPE_FILE
        mapMessage[CHILD_ID] = messageKey
        mapMessage[CHILD_TEXT] = name
        mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
        mapMessage[CHILD_IMAGE_URL] = url
        mapMessage[CHILD_SIZE] = size

        loadFileMessageToStorage(receivingUserId, messageKey, mapMessage)
        onSuccess()
    }

    private fun loadFileMessageToStorage(
        receivingUserId: String,
        messageKey: String,
        mapMessage: HashMap<String, Any>,
    ) {
        val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
        val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"

        val mapDialog = hashMapOf<String, Any>()
        mapDialog["$refDialogUser/$messageKey"] = mapMessage
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

        DATABASE_REFERENCE.updateChildren(mapDialog)
            .addOnFailureListener {
                Log.e(TAG_USER_MODEL, "Load file error: " + it.message.toString())
            }
    }

    override suspend fun createMessageKey(uid: String, onSuccess: (messageKey: String) -> Unit) {
        val mMessageKey =
            DATABASE_REFERENCE.child(NODE_MESSAGES).child(UID).child(uid).push().key.toString()
        onSuccess(mMessageKey)
    }

    override fun deleteMessageForAll(model: UserModel, messageKey: String, function: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_MESSAGES).child(UID).child(model.uid.toString())
            .child(messageKey).removeValue()
            .addOnFailureListener {
                Log.e(TAG_SINGLE_CHAT, "Deleting message error: " + it.message.toString())
            }
        DATABASE_REFERENCE.child(NODE_MESSAGES).child(model.uid.toString()).child(UID)
            .child(messageKey).removeValue()
            .addOnFailureListener {
                Log.e(TAG_SINGLE_CHAT, "Deleting message error: " + it.message.toString())
            }
            .addOnSuccessListener {
                function()
            }
    }

    override fun editMessage(
        map: Map<String, String>,
        messageId: String,
        contactId: String,
        onSuccess: () -> Unit
    ) {
        DATABASE_REFERENCE.child(NODE_MESSAGES).child(UID).child(contactId).child(messageId)
            .updateChildren(map)
            .addOnFailureListener {
                Log.e(TAG_SINGLE_CHAT, "Edit message error: " + it.message.toString())
            }
        DATABASE_REFERENCE.child(NODE_MESSAGES).child(contactId).child(UID).child(messageId)
            .updateChildren(map)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_SINGLE_CHAT, "Edit message error: " + it.message.toString())
            }
    }

    override fun checkForBlock(model: UserModel, function: (blocked: Boolean) -> Unit) {
        DATABASE_REFERENCE.child(NODE_BLACK_LIST).child(model.uid.toString()).child(UID).addListenerForSingleValueEvent(
            AppValueEventListener{
            if(it.exists()){
                function(true)
            }
            else{
                function(false)
            }
        })
    }

    override fun removeListener() {
        DATABASE_REFERENCE.child(NODE_MESSAGES).child(UID).child(path)
            .removeEventListener(mListenerGetMessages)
    }
}