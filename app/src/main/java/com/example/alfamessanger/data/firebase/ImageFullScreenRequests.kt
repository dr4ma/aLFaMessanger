package com.example.alfamessanger.data.firebase

import android.util.Log
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.domain.repository.ImageFullScreenRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener

class ImageFullScreenRequests : ImageFullScreenRepository {

    private var listAllPhotos: MutableList<SavedPhotoModel> = mutableListOf()
    private lateinit var listenerAllPhotos: AppValueEventListener

    override fun saveImage(map: Map<String, String>, url: String, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_SAVED).child(UID).child(url).setValue(map)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_IMAGE_FULL_SCREEN, it.message.toString())
            }
    }

    override fun deleteFromSaved(model: SavedPhotoModel, onSuccess: () -> Unit) {
        DATABASE_REFERENCE.child(NODE_SAVED).child(UID).child(model.id).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG_IMAGE_FULL_SCREEN, "Delete from saved error: " + it.message.toString())
            }
    }
    override fun getSavedPhotos(onSuccess: (list: MutableList<SavedPhotoModel>) -> Unit) {
        listenerAllPhotos = AppValueEventListener { child ->
            if(child.exists()){
                listAllPhotos.clear()
                for(friend in child.children){
                    listAllPhotos.add(friend.getValue(SavedPhotoModel::class.java) ?: SavedPhotoModel())
                }
            }
            onSuccess(listAllPhotos)
        }
        DATABASE_REFERENCE.child(NODE_SAVED).child(UID).addValueEventListener(listenerAllPhotos)
    }

}