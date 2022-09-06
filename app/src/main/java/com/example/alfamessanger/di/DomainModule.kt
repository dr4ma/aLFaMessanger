package com.example.alfamessanger.di

import com.example.alfamessanger.domain.repository.*
import com.example.alfamessanger.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideAuthUseCase(): AuthUseCase {
        return AuthUseCase()
    }

    @Provides
    fun provideGetCodeUseCase(repository: RegisterRepository): GetCodeUseCase {
        return GetCodeUseCase(registerRepository = repository)
    }

    @Provides
    fun provideGetUserModelUseCase(repository: GetUserModelRepository): GetUserModelUseCase {
        return GetUserModelUseCase(getUserModelRepository = repository)
    }

    @Provides
    fun provideLoadPicUseCase(
        repository: FileStorageRepository,
        singleChatRepository: SingleChatRepository
    ): LoadPicUseCase {
        return LoadPicUseCase(
            loadFileRepository = repository,
            singleChatRepository = singleChatRepository
        )
    }

    @Provides
    fun provideSearchHistory(repository: SearchUsersHistoryRepository): SearchUsersHistoryUseCase {
        return SearchUsersHistoryUseCase(searchUsersHistoryRepository = repository)
    }

    @Provides
    fun provideSingleChatUseCase(
        repository: SingleChatRepository,
        fileStorageRepository: FileStorageRepository,
        userModelRepository: GetUserModelRepository
    ): SingleChatUseCase {
        return SingleChatUseCase(
            singleChatRepository = repository,
            fileStorageRepository = fileStorageRepository,
            userModelRepository = userModelRepository
        )
    }

    @Provides
    fun provideVoiceRecordIseCase(
        repository: SingleChatRepository,
        fileStorageRepository: FileStorageRepository
    ): VoiceRecorderUseCase {
        return VoiceRecorderUseCase(
            singleChatRepository = repository,
            fileStorageRepository = fileStorageRepository
        )
    }

    @Provides
    fun provideMyChatsUseCase(
        repository: MyChatsRepository,
        userModelRepository: GetUserModelRepository
    ): MyChatsUseCase {
        return MyChatsUseCase(
            myChatsRepository = repository,
            userModelRepository = userModelRepository
        )
    }

    @Provides
    fun provideUserProfileUseCase(repository: UserProfileRepository, singleChatRepository: SingleChatRepository): UserProfileUseCase {
        return UserProfileUseCase(userProfileRepository = repository, singleChatRepository = singleChatRepository)
    }

    @Provides
    fun provideFriendsListUseCase(repository: FriendsListRepository): FriendsListUseCase {
        return FriendsListUseCase(friendsListRepository = repository)
    }

    @Provides
    fun provideImageFullScreenUseCase(
        repository: ImageFullScreenRepository,
        singleChatRepository: SingleChatRepository,
        savedRepository: SavedRepository
    ): ImageFullScreenUseCase {
        return ImageFullScreenUseCase(
            imageFullScreenRepository = repository,
            singleChatRepository = singleChatRepository,
            savedRepository = savedRepository
        )
    }

    @Provides
    fun provideSavedUseCase(
        repository: SavedRepository
    ): SavedUseCase {
        return SavedUseCase(
            savedRepository = repository
        )
    }

    @Provides
    fun provideBlackListUseCase(
        repository: BlackListRepository
    ): BlackListUseCase {
        return BlackListUseCase(
            blackListRepository = repository
        )
    }

    @Provides
    fun provideNotificationsUseCase(
        repository: NotificationsRepository
    ): NotificationsUseCase {
        return NotificationsUseCase(
            notificationsRepository = repository
        )
    }
}