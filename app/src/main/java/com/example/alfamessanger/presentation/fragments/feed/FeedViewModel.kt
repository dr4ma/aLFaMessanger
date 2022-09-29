package com.example.alfamessanger.presentation.fragments.feed

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedModel
import com.example.alfamessanger.domain.models.FeedNewsModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.FeedUseCase
import com.example.alfamessanger.utills.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val feedUseCase: FeedUseCase) : ViewModel() {

    val feed: MutableLiveData<MutableList<FeedNewsModel>> = MutableLiveData()

    fun getFeedNews(onSuccess: (MutableList<FeedNewsModel>) -> Unit) {
        viewModelScope.launch {
            feedUseCase.getFeedNews() {
            }
                .collect {
                    onSuccess(it)
                }
        }
    }

    fun getForNewNotifications(onSuccess: (newNotifications : Boolean) -> Unit){
        feedUseCase.getForNewNotifications {
            onSuccess(it)
        }
    }

    fun watchNotifications(){
        feedUseCase.watchNotifications()
    }
}