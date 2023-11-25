package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.local.LocalPostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    @Provides
    fun provideLocalDataSource(dao: LocalPostDao): LocalDataSource {
        return LocalDataPostSource(dao)
    }
}