package com.example.alfamessanger.domain.repository

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView
import java.io.File

interface SingleChatRepository {
    val message : MutableLiveData<MessageModel>
        get() = MutableLiveData()
    val messageDelete : MutableLiveData<MessageModel>
        get() = MutableLiveData()
    suspend fun sendMessage(sendingMap : HashMap<String, Any>, onSuccess: () -> Unit)
    fun getMessages(pathUser : String, countMessages : Int, function:() -> Unit)
    fun createChat(userModel : UserModel, messageType : String, message: String, receivingId : String, time : Any)
    fun sendImageMessage(receivingUserId : String, imageUrl : String, messageKey : String, bitmapImage : Bitmap, onSuccess:() -> Unit)
    fun uploadVoiceMessage(receivingUserId: String, url: String, messageKey: String, duration: Int, onSuccess: () -> Unit)
    suspend fun sendFile(receivingUserId: String, url: String, messageKey: String, name : String, size : Double, onSuccess: () -> Unit)
    suspend fun createMessageKey(uid : String, onSuccess: (messageKey : String) -> Unit)
    fun deleteMessageForAll(model : UserModel, messageKey: String, function: () -> Unit)
    fun editMessage(map : Map<String, String>, messageId: String, contactId : String, onSuccess: () -> Unit)
    fun checkForBlock(model: UserModel, function: (blocked:Boolean) -> Unit)
    fun removeListener()
}