package com.example.alfamessanger.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.alfamessanger.domain.models.SearchHistoryUserModel
import com.example.alfamessanger.data.room.RoomDatabase

@Database(entities = [SearchHistoryUserModel::class], version = 2, exportSchema = false)
abstract class RoomDatabase : androidx.room.RoomDatabase() {

    abstract fun getSearchHistoryDao() : SearchHistoryDao

    companion object {

        @Volatile
        private var database: RoomDatabase? = null

        fun getInstance(context: Context): RoomDatabase {
            return if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    RoomDatabase::class.java,
                    "database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                database as RoomDatabase
            } else{
                database as RoomDatabase
            }

        }
    }
}