package com.wasingun.seller_lounge.di

import com.wasingun.seller_lounge.data.datasource.NaverApiRemoteDataSource
import com.wasingun.seller_lounge.data.datasource.NaverApiDataSource
import com.wasingun.seller_lounge.network.NaverApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NaverApiModule {

    @Provides
    fun provideDataSource(apiClient: NaverApiClient): NaverApiDataSource {
        return NaverApiRemoteDataSource(apiClient)
    }
}