package com.example.alfamessanger.data.room

import com.example.alfamessanger.domain.models.SearchHistoryUserModel
import com.example.alfamessanger.domain.repository.SearchUsersHistoryRepository
import javax.inject.Inject

class SearchUsersHistoryRequests @Inject constructor(private val historyDao : SearchHistoryDao) : SearchUsersHistoryRepository{

    override suspend fun getSearchHistory(): MutableList<SearchHistoryUserModel> {
        return historyDao.getSearchHistory()
    }

    override suspend fun insert(history: MutableList<SearchHistoryUserModel>) {
        historyDao.insert(history = history)
    }

    override suspend fun update(history: MutableList<SearchHistoryUserModel>) {
        historyDao.update(history)
    }

    override fun delete() {
        historyDao.delete()
    }
}