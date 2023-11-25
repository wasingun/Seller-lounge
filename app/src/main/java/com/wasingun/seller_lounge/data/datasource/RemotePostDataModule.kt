package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.network.PostDataClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemotePostDataModule {

    @Provides
    fun provideDataSource(apiClient: PostDataClient): RemotePostDataSource {
        return RemotePostFirebaseDataSource(apiClient)
    }
}