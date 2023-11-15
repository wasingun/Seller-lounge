package com.wasingun.seller_lounge.data.datasource

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