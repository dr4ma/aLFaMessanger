package com.example.alfamessanger.presentation.activities.mainActivity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.utills.enums.StatusConnection
import com.example.alfamessanger.utills.networkConnection.ConnectivityObserver
import com.example.alfamessanger.utills.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val connectivityObserver: ConnectivityObserver) : ViewModel() {

    fun observConnection(function: (status: StatusConnection) -> Unit) {
        viewModelScope.launch {
            val status = connectivityObserver.observe()
            status.collect {
                function(it)
            }
        }
    }
}