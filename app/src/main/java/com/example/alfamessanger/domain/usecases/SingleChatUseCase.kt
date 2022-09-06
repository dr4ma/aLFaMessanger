package com.example.alfamessanger.domain.usecases

import android.net.Uri
import android.os.Environment
import android.os.health.UidHealthStats
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.alfamessanger.domain.models.MessageModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.FileStorageRepository
import com.example.alfamessanger.domain.repository.GetUserModelRepository
import com.example.alfamessanger.domain.repository.SingleChatRepository
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView
import com.example.alfamessanger.utills.*
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.math.sin

class SingleChatUseCase @Inject constructor(
    private val singleChatRepository: SingleChatRepository,
    private val fileStorageRepository: FileStorageRepository,
    private val userModelRepository: GetUserModelRepository
) {

    private lateinit var mObserverLoadMessage: Observer<MessageModel>
    private lateinit var mObserverDeleteMessage: Observer<MessageModel>
    val message: MutableLiveData<MessageModel> = MutableLiveData()
    val messageDelete: MutableLiveData<MessageModel> = MutableLiveData()

    suspend fun sendMessage(
        userModel: UserModel,
        message: String,
        receivingUserId: String,
        typeText: String,
        onSuccess: () -> Unit
    ) {
        val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
        val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"
        val messageKey = DATABASE_REFERENCE.child(refDialogUser).push().key
        val time =  ServerValue.TIMESTAMP

        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = UID
        mapMessage[CHILD_TYPE] = typeText
        mapMessage[CHILD_TEXT] = message
        mapMessage[CHILD_ID] = messageKey.toString()
        mapMessage[CHILD_TIMESTAMP] = time

        val mapDialog = hashMapOf<String, Any>()
        mapDialog["$refDialogUser/$messageKey"] = mapMessage
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

        singleChatRepository.sendMessage(mapDialog) {
            onSuccess()
        }
        singleChatRepository.createChat(userModel, TYPE_TEXT, message, receivingUserId, time)
    }

    fun editMessage(map : Map<String, String>, messageId : String, contactId : String, onSuccess: () -> Unit){
        singleChatRepository.editMessage(map, messageId, contactId){
            onSuccess()
        }
    }

    fun getMessage(pathUser: String, countMessages: Int, function: () -> Unit) {
        mObserverLoadMessage = Observer {
            message.value = it
        }
        singleChatRepository.message.observe(APP_ACTIVITY_MAIN, mObserverLoadMessage)
        singleChatRepository.getMessages(pathUser, countMessages) {
            function()
        }
    }

    fun getOtherUserModel(id : String, onSuccess: (model : UserModel) -> Unit){
        userModelRepository.getOtherUserModel(id) {
            onSuccess(it)
        }
    }

    suspend fun sendFile(
        model: UserModel,
        receivingId: String,
        uri: Uri,
        messageKey: String,
        name: String,
        size: Double,
        onSuccess: () -> Unit
    ) {
        val path = STORAGE_REFERENCE.child(FOLDER_MESSAGES_FILES).child(messageKey)
        fileStorageRepository.putFileToStorage(uri, path) {
            fileStorageRepository.getUrlFromStorage(path) { path ->
                GlobalScope.launch(Dispatchers.IO) {
                    singleChatRepository.sendFile(receivingId, path, messageKey, name, size){
                        val time =  ServerValue.TIMESTAMP
                        singleChatRepository.createChat(model, TYPE_FILE, "", model.uid.toString(), time)
                        onSuccess()
                    }
                }
            }
        }
    }

    suspend fun createMessageKey(uid: String, onSuccess: (messageKey: String) -> Unit) {
        singleChatRepository.createMessageKey(uid) { messageKey ->
            onSuccess(messageKey)
        }
    }

    fun downloadFile(path: String, text: String, function: () -> Unit) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            text
        )
        try {
            if (AppPermissions.checkPermission(WRITE_FILES)) {
                file.createNewFile()
                fileStorageRepository.getFileFromStorage(file, path) {
                    function()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG_FILE_STORAGE, "Download file error: " + e.message.toString())
        }
    }

    fun deleteMessageForAll(model : UserModel, messageKey: String, function: () -> Unit){
        singleChatRepository.deleteMessageForAll(model, messageKey){
            function()
        }
    }

    fun checkForBlockMe(model: UserModel, function: (blocked : Boolean) -> Unit){
        singleChatRepository.checkForBlock(model){
            function(it)
        }
    }

    fun observDeleteMessage(){
        mObserverDeleteMessage = Observer {
            messageDelete.value = it
        }
        singleChatRepository.messageDelete.observe(APP_ACTIVITY_MAIN, mObserverDeleteMessage)
    }

    fun deleteFileFromStorage(path :String, fileName : String){
        val file = File(APP_ACTIVITY_MAIN.filesDir, fileName)
        if(file.exists()){
            file.delete()
        }
        fileStorageRepository.removeFileFromStorage(path)
    }

    fun removeObservers() {
        singleChatRepository.message.removeObserver(mObserverLoadMessage)
        singleChatRepository.messageDelete.removeObserver(mObserverDeleteMessage)
        singleChatRepository.removeListener()
    }
}