package com.example.alfamessanger.data.firebase

import android.net.Uri
import android.util.Log
import com.example.alfamessanger.domain.repository.FileStorageRepository
import com.example.alfamessanger.utills.*
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import java.io.File

class FileStorageRequests : FileStorageRepository {
    override fun putFileToStorage(uri: Uri, path: StorageReference, function: () -> Unit) {
        path.putFile(uri)
            .addOnSuccessListener {
                function()
            }
            .addOnFailureListener {
                Log.e(TAG_FILE_STORAGE, "Put image to storage: " + it.message.toString())
            }
    }

    override fun getUrlFromStorage(path: StorageReference, function: (url: String) -> Unit) {
        path.downloadUrl
            .addOnSuccessListener {
                function(it.toString())
            }
            .addOnFailureListener {
                Log.e(TAG_FILE_STORAGE, "Get url from storage: " + it.message.toString())
            }
    }

    override fun putUrlToDatabase(url: String, function: (url: String) -> Unit) {
        val urlMap = mapOf(
            CHILD_ICON_URL to url
        )
        DATABASE_REFERENCE.child(NODE_USERS).child(UID).updateChildren(urlMap)
            .addOnSuccessListener {
                function(url)
            }
            .addOnFailureListener {
                Log.e(TAG_FILE_STORAGE, "Put url to database error: " + it.message.toString())
            }
    }

    override fun getFileFromStorage(file: File, fileUrl: String, function: () -> Unit) {
        val path = STORAGE_REFERENCE.storage.getReferenceFromUrl(fileUrl)
        path.getFile(file)
//            .addOnProgressListener { it ->
//                val value = (100.0 * it.bytesTransferred) / it.totalByteCount
//                showToast(value.toString())
//            }
            .addOnSuccessListener {
                function()
            }
            .addOnFailureListener {
                function()
                Log.e(TAG_FILE_STORAGE, "Download file error: " + it.message.toString())
            }
    }

    override fun removeFileFromStorage(fileUrl: String) {
        val path = STORAGE_REFERENCE.storage.getReferenceFromUrl(fileUrl)
        path.delete()
            .addOnFailureListener {
                Log.e(TAG_FILE_STORAGE, "Delete file from storage error: " + it.message.toString())
            }
    }
}