package com.example.alfamessanger.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "search_history_table")
data class SearchHistoryUserModel(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    var uid: String? = "",
    var phone: String = "",
    var name: String = "",
    var nickname: String = "",
    var bio: String = "",
    var status: String = "",
    var iconUrl: String = ""
) : Serializable
