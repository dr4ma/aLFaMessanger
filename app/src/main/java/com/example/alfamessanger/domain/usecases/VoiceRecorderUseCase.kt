package com.example.alfamessanger.domain.usecases

import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.FileStorageRepository
import com.example.alfamessanger.domain.repository.SingleChatRepository
import com.example.alfamessanger.utills.*
import com.google.firebase.database.ServerValue
import java.io.File
import java.time.Duration
import javax.inject.Inject

class VoiceRecorderUseCase @Inject constructor(
    private val singleChatRepository: SingleChatRepository,
    private val fileStorageRepository: FileStorageRepository
) {

    private val mMediaRecorder = MediaRecorder()
    private lateinit var mFile: File
    private lateinit var mMessageKey: String
    private lateinit var mUidUser : String

    private val mMediaMetaData = MediaMetadataRetriever()

    suspend fun startRecord(uid: String) {
        mUidUser = uid
        try {
            singleChatRepository.createMessageKey(uid) { messageKey ->
                mMessageKey = messageKey
                createFileForRecord()
                prepareMediaRecorder()
                mMediaRecorder.start()
            }

        } catch (e: Exception) {
            Log.e(TAG_VOICE_RECORD, "Start voice recorder error: " + e.message.toString())
        }
    }

    private fun prepareMediaRecorder() {
        mMediaRecorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(mFile.absolutePath)
            prepare()
        }
    }

    suspend fun stopRecord(model : UserModel, onSuccess: () -> Unit) {
        try {
            stopMediaRecorder() { file, messageKey ->
                if (messageKey.isNotEmpty()) {
                    mMediaMetaData.setDataSource(APP_ACTIVITY_MAIN.applicationContext, Uri.fromFile(mFile))
                    val duration = mMediaMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    if (duration != null) {
                        uploadMessageVoice(model, file, messageKey, duration.toInt()){
                            onSuccess()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG_VOICE_RECORD, "Stop voice recorder error: " + e.message.toString())
            mFile.delete()
        }
    }

    private fun stopMediaRecorder(onSuccess: (file: File, messageKey: String) -> Unit) {
        mMediaRecorder.stop()
        onSuccess(mFile, mMessageKey)
    }

    fun releaseRecorder() {
        try {
            mMediaRecorder.release()
        } catch (e: Exception) {
            Log.e(TAG_VOICE_RECORD, "Release voice recorder error: " + e.message.toString())
        }
    }

    fun cancelRecordAudio(){
        try {
            mMediaRecorder.stop()
            mMediaRecorder.release()
            mFile.delete()
        }
        catch (e : Exception){
            Log.e(TAG_VOICE_RECORD, "Cancel voice recorder error: " + e.message.toString())
        }
    }

    private fun createFileForRecord() {
        mFile = File(APP_ACTIVITY_MAIN.filesDir, mMessageKey)
        mFile.createNewFile()
    }

    private fun uploadMessageVoice(model : UserModel, file: File, messageKey: String, duration: Int, onSuccess: () -> Unit) {
        val path = STORAGE_REFERENCE.child(FOLDER_MESSAGES_VOICE).child(messageKey)
        fileStorageRepository.putFileToStorage(Uri.fromFile(file), path) {
            fileStorageRepository.getUrlFromStorage(path) { path ->
                singleChatRepository.uploadVoiceMessage(mUidUser, path, messageKey, duration){
                    val time =  ServerValue.TIMESTAMP
                    singleChatRepository.createChat(model, TYPE_VOICE, "", model.uid.toString(), time)
                    onSuccess()
                }
            }
        }

    }
}