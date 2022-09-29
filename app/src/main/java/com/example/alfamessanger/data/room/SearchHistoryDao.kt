package com.example.alfamessanger.data.room

import androidx.room.*
import com.example.alfamessanger.domain.models.SearchHistoryUserModel

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history_table")
    fun getSearchHistory() : MutableList<SearchHistoryUserModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: MutableList<SearchHistoryUserModel>)

    @Update
    fun update (history: MutableList<SearchHistoryUserModel>)

    @Query("DELETE FROM search_history_table")
    fun delete()
}