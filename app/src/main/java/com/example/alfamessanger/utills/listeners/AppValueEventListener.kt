package com.example.alfamessanger.utills.listeners

import android.util.Log
import com.example.alfamessanger.utills.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AppValueEventListener (val onSuccess:(DataSnapshot) -> Unit) : ValueEventListener {
    override fun onCancelled(p0: DatabaseError) {
        Log.e(TAG, "listener error: " + p0.message)
    }

    override fun onDataChange(p0: DataSnapshot) {
        onSuccess(p0)
    }

}