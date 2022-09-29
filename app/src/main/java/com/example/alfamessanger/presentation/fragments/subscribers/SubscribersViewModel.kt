package com.example.alfamessanger.presentation.fragments.subscribers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.usecases.SubscribersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribersViewModel @Inject constructor(private val subscribersUseCase: SubscribersUseCase) : ViewModel() {

    val subs: MutableLiveData<MutableList<UserModel>> = MutableLiveData()

    fun getSubs(channelId: String) {
        viewModelScope.launch {
            subscribersUseCase.getSubs(channelId)
                .collect {
                    subs.value = it
                }
        }
    }
}