package com.wasingun.seller_lounge.di

import com.wasingun.seller_lounge.data.datasource.SearchContentRemoteDataSource
import com.wasingun.seller_lounge.data.repository.SearchContentDataSource
import com.wasingun.seller_lounge.network.NaverApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SearchContentModule {

    @Provides
    fun provideDataSource(apiClient: NaverApiClient): SearchContentDataSource {
        return SearchContentRemoteDataSource(apiClient)
    }
}