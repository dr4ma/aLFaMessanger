package com.example.alfamessanger.presentation.fragments.myChannels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.MyChannelsUseCase
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChannelsViewModel @Inject constructor(private val myChannelsUseCase: MyChannelsUseCase) : ViewModel() {

    private lateinit var mObserverChannels: Observer<MutableList<ChannelModel>>
    val channels: MutableLiveData<MutableList<ChannelModel>> = MutableLiveData()

    fun getMyChannels(onSuccess:() -> Unit){
        viewModelScope.launch {
            mObserverChannels = Observer {
                channels.value = it
            }
            myChannelsUseCase.channels.observe(APP_ACTIVITY_MAIN, mObserverChannels)
            myChannelsUseCase.getMyChannels{
                onSuccess()
            }
        }
    }
}