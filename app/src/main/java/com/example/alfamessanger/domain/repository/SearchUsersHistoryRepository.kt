package com.example.alfamessanger.domain.repository

import com.example.alfamessanger.domain.models.SearchHistoryUserModel

interface SearchUsersHistoryRepository {

    suspend fun getSearchHistory() : MutableList<SearchHistoryUserModel>
    suspend fun insert(history: MutableList<SearchHistoryUserModel>)
    suspend fun update(history: MutableList<SearchHistoryUserModel>)
    fun delete()
}