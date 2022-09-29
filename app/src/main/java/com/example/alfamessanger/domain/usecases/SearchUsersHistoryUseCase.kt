package com.example.alfamessanger.domain.usecases

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.alfamessanger.domain.models.SearchHistoryUserModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.SearchUsersHistoryRepository
import com.example.alfamessanger.utills.showToast
import kotlinx.coroutines.*
import javax.inject.Inject

class SearchUsersHistoryUseCase @Inject constructor(private val searchUsersHistoryRepository: SearchUsersHistoryRepository) {

    val historyList: MutableLiveData<MutableList<SearchHistoryUserModel>> = MutableLiveData()
    private var checkNick = false

    fun getHistory() {
        GlobalScope.launch(Dispatchers.IO) {
            historyList.postValue(searchUsersHistoryRepository.getSearchHistory())
        }
    }

    fun insertUserInHistory(model: UserModel) {
        checkNick = false
        run breaking@{
            if (historyList.value?.isEmpty() == true){
                checkNick = true
            }
            else {
                historyList.value?.forEach {
                    if (model.nickname != it.nickname) {
                        checkNick = true
                    } else {
                        checkNick = false
                        return@breaking
                    }
                }
            }
        }
        if(checkNick){
            //deleteHistory()
            val historyModel = SearchHistoryUserModel(
                id = historyList.value?.size ?: 0,
                uid = model.uid,
                phone = model.phone,
                name = model.name,
                nickname = model.nickname,
                bio = model.bio,
                status = model.status,
                iconUrl = model.iconUrl
            )
            GlobalScope.launch(Dispatchers.IO) {
                if (historyList.value?.size!! < 5) {
                    historyList.value!!.add(historyModel)
                    searchUsersHistoryRepository.insert(history = historyList.value!!)
                } else {
                    historyList.value!!.add(historyModel)
                    historyList.value!!.removeAt(0)
                    historyList.value!![0].id = 0
                    historyList.value!![1].id = 1
                    historyList.value!![2].id = 2
                    historyList.value!![3].id = 3
                    historyList.value!![4].id = 4
                    //deleteHistory()
                    searchUsersHistoryRepository.update(history = historyList.value!!)
                }

            }
        }
    }

    private fun deleteHistory() {
        GlobalScope.launch(Dispatchers.IO) {
            searchUsersHistoryRepository.delete()
        }
    }
}