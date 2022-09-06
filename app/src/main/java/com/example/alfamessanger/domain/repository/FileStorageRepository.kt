package com.example.alfamessanger.domain.repository

import android.net.Uri
import com.google.firebase.storage.StorageReference
import java.io.File

interface FileStorageRepository {
    fun putFileToStorage(uri: Uri, path: StorageReference, function: () -> Unit)
    fun getUrlFromStorage(path: StorageReference, function: (url: String) -> Unit)
    fun putUrlToDatabase(url: String, function: (url: String) -> Unit)
    fun getFileFromStorage(file: File, fileUrl: String, function: () -> Unit)
    fun removeFileFromStorage(fileUrl: String)
}