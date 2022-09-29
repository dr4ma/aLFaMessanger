package com.example.alfamessanger.di

import android.app.Application
import com.example.alfamessanger.data.room.RoomDatabase
import com.example.alfamessanger.data.room.SearchHistoryDao
import com.example.alfamessanger.data.room.SearchUsersHistoryRequests
import com.example.alfamessanger.domain.repository.SearchUsersHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(context: Application): RoomDatabase {
        return RoomDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideDao(roomDatabase : RoomDatabase) : SearchHistoryDao{
        return roomDatabase.getSearchHistoryDao()
    }

    @Singleton
    @Provides
    fun provideSearchHistoryRepository(dao : SearchHistoryDao) : SearchUsersHistoryRepository{
        return SearchUsersHistoryRequests(historyDao = dao)
    }
}