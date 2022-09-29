package com.example.alfamessanger.presentation.fragments.singleChat

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.LoadPicUseCase
import com.example.alfamessanger.domain.usecases.NotificationsUseCase
import com.example.alfamessanger.domain.usecases.SingleChatUseCase
import com.example.alfamessanger.domain.usecases.VoiceRecorderUseCase
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView
import com.example.alfamessanger.utills.*
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleChatViewModel @Inject constructor(
    private val singleChatUseCase: SingleChatUseCase,
    private val loadPicUseCase: LoadPicUseCase,
    private val voiceRecorderUseCase: VoiceRecorderUseCase,
    private val notificationsUseCase: NotificationsUseCase
) : ViewModel() {

    private lateinit var mObserverLoadMessage: Observer<MessageModel>
    private lateinit var mObserverDeleteMessage: Observer<MessageModel>
    val message: MutableLiveData<MessageModel> = MutableLiveData()
    val messageDelete: MutableLiveData<MessageModel> = MutableLiveData()

    fun sendMessage(
        userModel : UserModel,
        message: String,
        receivingUserId: String,
        typeText: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            singleChatUseCase.sendMessage(userModel, message, receivingUserId, typeText) {
                onSuccess()
            }
        }
    }

    fun editMessage(contactId : String, messageId : String, text : String, onSuccess :() -> Unit){
        val mapOfData = mapOf(
            CHILD_TEXT to text
        )

        singleChatUseCase.editMessage(mapOfData, messageId, contactId) {
            onSuccess()
        }
    }

    fun getMessage(pathUser: String, countMessages: Int, function:() -> Unit) {
        mObserverLoadMessage = Observer {
            message.value = it
        }
        singleChatUseCase.message.observe(APP_ACTIVITY_MAIN, mObserverLoadMessage)
        singleChatUseCase.getMessage(pathUser, countMessages) {
            function()
        }
    }

    fun loadPic(
        model : UserModel,
        messageKey: String,
        receivingId: String,
        uri: Uri,
        path: StorageReference,
        bitmapImage: Bitmap,
        onSuccess: () -> Unit
    ) {
        loadPicUseCase.loadPicMessage(model, messageKey, receivingId, uri, path, bitmapImage) {
            onSuccess()
        }
    }

    fun sendFile(model:UserModel,receivingId: String, uri: Uri, messageKey: String, name : String, size : Double, onSuccess: () -> Unit){
        viewModelScope.launch {
            singleChatUseCase.sendFile(model, receivingId, uri, messageKey, name, size){
                onSuccess()
            }
        }
    }

    fun getUserModel(uid : String, onSuccess: (model : UserModel) -> Unit){
        singleChatUseCase.getOtherUserModel(uid){
            onSuccess(it)
        }
    }

    fun startRecordVoiceMessage(uid: String) {
        viewModelScope.launch {
            voiceRecorderUseCase.startRecord(uid)
        }
    }

    fun stopRecordVoiceMessage(model : UserModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            voiceRecorderUseCase.stopRecord(model){
                onSuccess()
            }
        }
    }

    fun cancelRecord() {
        voiceRecorderUseCase.cancelRecordAudio()
    }

    fun createMessageKey(uid: String, onSuccess: (messageKey: String) -> Unit) {
        viewModelScope.launch {
            singleChatUseCase.createMessageKey(uid) { messageKey ->
                onSuccess(messageKey)
            }
        }
    }

    fun checkForBlock(model : UserModel, function: (blocked : Boolean) -> Unit){
        singleChatUseCase.checkForBlockMe(model){
            function(it)
        }
    }

    fun releaseRecorder() {
        voiceRecorderUseCase.releaseRecorder()
    }

    fun sendNotification(userModel: UserModel, tittle: String, message: String) {
        viewModelScope.launch {
            notificationsUseCase.sendNotification(userModel, tittle, message)
        }
    }

    fun observDeleteMessage(){
        mObserverDeleteMessage = Observer {
            messageDelete.value = it
        }
        singleChatUseCase.messageDelete.observe(APP_ACTIVITY_MAIN, mObserverDeleteMessage)
        singleChatUseCase.observDeleteMessage()
    }


    fun removeObservers() {
        singleChatUseCase.message.removeObserver(mObserverLoadMessage)
        singleChatUseCase.messageDelete.removeObserver(mObserverDeleteMessage)
        singleChatUseCase.removeObservers()
    }
}