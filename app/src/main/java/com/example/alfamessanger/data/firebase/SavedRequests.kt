package com.example.alfamessanger.data.firebase

import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.domain.repository.SavedRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.listeners.AppValueEventListener

class SavedRequests : SavedRepository {

    private var listAllPhotos: MutableList<SavedPhotoModel> = mutableListOf()
    override val photosList: MutableLiveData<MutableList<SavedPhotoModel>> = MutableLiveData()
    private lateinit var listenerAllPhotos: AppValueEventListener


    override fun getSavedPhotos(function: () -> Unit) {
        listenerAllPhotos = AppValueEventListener { child ->
            if(child.exists()){
                listAllPhotos.clear()
                for(friend in child.children){
                    listAllPhotos.add(friend.getValue(SavedPhotoModel::class.java) ?: SavedPhotoModel())
                }
                photosList.value = listAllPhotos
            }
        }
        DATABASE_REFERENCE.child(NODE_SAVED).child(UID).addValueEventListener(listenerAllPhotos)
        DATABASE_REFERENCE.child(NODE_SAVED).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                if(!it.exists()){
                    photosList.value?.clear()
                    function()
                }
            })
    }
}