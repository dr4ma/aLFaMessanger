package com.example.alfamessanger.presentation.fragments.graffiti

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.usecases.SingleChatUseCase
import com.example.alfamessanger.utills.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GraffitiViewModel @Inject constructor(private val singleChatUseCase: SingleChatUseCase) :
    ViewModel() {

    private lateinit var fileOutPutStream: OutputStream
    private lateinit var mFile: File
    private lateinit var uriGraffiti: Uri

    fun saveBitmap(bitmap: Bitmap, onSuccess: () -> Unit) {
        if (AppPermissions.checkPermission(WRITE_FILES)) {
            val file_name: String = UUID.randomUUID().toString() + ".png"
            val folder: File
            var saved: Boolean = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                folder = File(
                    APP_ACTIVITY_MAIN.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        .toString() + File.separator + APP_ACTIVITY_MAIN.getString(
                        R.string.app_name
                    )
                )
            } else {
                folder = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .toString() + File.separator + APP_ACTIVITY_MAIN.getString(
                        R.string.app_name
                    )
                )
            }

            if (folder.exists()) {
                folder.mkdirs()
            }
            val image = File(folder.toString() + File.separator + file_name)
            val imageUri = Uri.fromFile(image)
            try {
                fileOutPutStream = FileOutputStream(image)
                saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutPutStream)
            } catch (e: Exception) {
                Log.e(TAG_SAVE_FILE, e.message.toString())
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = APP_ACTIVITY_MAIN.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, file_name)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + APP_ACTIVITY_MAIN.getString(R.string.app_name)
                )
                val uri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fileOutPutStream = uri?.let { resolver.openOutputStream(it) }!!
                saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutPutStream)
            } else {
                sendPictureToGallery(imageUri)
            }

            if (saved) {
                onSuccess()
            } else {
                showToast(APP_ACTIVITY_MAIN.getString(R.string.error_save_graffiti))
            }
            fileOutPutStream.flush()
            fileOutPutStream.close()
        }
    }

    private fun sendPictureToGallery(imageUri: Uri?) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.setData(imageUri)
    }

    fun sendGraffitiInChat(bitmap: Bitmap, mUid: String) : Uri {
        viewModelScope.launch {
            val scope = launch {
                singleChatUseCase.createMessageKey(mUid) { messageKey ->
                    mFile = File(APP_ACTIVITY_MAIN.filesDir, messageKey)
                    mFile.createNewFile()
                    val os = BufferedOutputStream(FileOutputStream(mFile))
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                    os.close()
                    uriGraffiti = Uri.fromFile(mFile)
                }
            }
            scope.join()
        }
        return uriGraffiti
    }
}