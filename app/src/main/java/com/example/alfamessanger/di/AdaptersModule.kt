package com.example.alfamessanger.di

import com.example.alfamessanger.domain.usecases.ChannelUseCase
import com.example.alfamessanger.domain.usecases.FeedUseCase
import com.example.alfamessanger.presentation.fragments.channel.ChannelAdapter
import com.example.alfamessanger.presentation.fragments.feed.FeedAdapter
import com.example.alfamessanger.presentation.fragments.mychats.MyChatsChannelsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdaptersModule {

    @Provides
    @Singleton
    fun provideFeedAdapter(feedUseCase: FeedUseCase, channelsUseCase: ChannelUseCase): FeedAdapter =
        FeedAdapter(feedUseCase, channelsUseCase)

    @Provides
    @Singleton
    fun provideChannelAdapter(channelsUseCase: ChannelUseCase): ChannelAdapter =
        ChannelAdapter(channelUseCase = channelsUseCase)

    @Provides
    @Singleton
    fun provideMyChannelsAdapter(feedUseCase: FeedUseCase): MyChatsChannelsAdapter =
        MyChatsChannelsAdapter(feedUseCase)
}