package com.example.alfamessanger.presentation.fragments.notifications

import androidx.lifecycle.ViewModel
import com.example.alfamessanger.domain.models.NotificationModel
import com.example.alfamessanger.domain.usecases.NotificationAppUseCase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationAppUseCase: NotificationAppUseCase) : ViewModel() {

    fun getNotificationsList(onSuccess: (list: MutableList<NotificationModel>) -> Unit){
        notificationAppUseCase.getNotificationsList {
            onSuccess(it)
        }
    }
}