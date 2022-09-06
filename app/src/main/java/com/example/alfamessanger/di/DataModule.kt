package com.example.alfamessanger.di

import com.example.alfamessanger.data.firebase.*
import com.example.alfamessanger.data.retrofit.NotificationsRequests
import com.example.alfamessanger.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun providePicStorageRepository() : FileStorageRepository{
        return FileStorageRequests()
    }

    @Provides
    fun provideRegisterRepository() : RegisterRepository{
        return RegisterRequests()
    }

    @Provides
    fun provideUserModelRepository() : GetUserModelRepository{
        return UserModelRequests()
    }

    @Provides
    fun provideSingleChatRepository() : SingleChatRepository{
        return SingleChatRequests()
    }

    @Provides
    fun provideMyChatsRepository() : MyChatsRepository{
        return MyChatsRequests()
    }


    @Provides
    fun provideUserProfileRepository() : UserProfileRepository{
        return UserProfileRequests()
    }


    @Provides
    fun provideFriendsListRepository() : FriendsListRepository{
        return FriendsListRequests()
    }

    @Provides
    fun provideImageFullScreenRepository() : ImageFullScreenRepository{
        return ImageFullScreenRequests()
    }

    @Provides
    fun provideSavedRepository() : SavedRepository{
        return SavedRequests()
    }

    @Provides
    fun provideBlackListRepository() : BlackListRepository{
        return BlackListRequests()
    }

    @Provides
    fun provideNotificationRepository() : NotificationsRepository{
        return NotificationsRequests()
    }
}