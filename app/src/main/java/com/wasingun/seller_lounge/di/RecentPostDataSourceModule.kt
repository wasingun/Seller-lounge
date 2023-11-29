package com.wasingun.seller_lounge.di

import com.wasingun.seller_lounge.data.datasource.RecentPostDataSource
import com.wasingun.seller_lounge.data.datasource.RecentPostLocalDataSource
import com.wasingun.seller_lounge.local.RecentPostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RecentPostDataSourceModule {

    @Provides
    fun provideLocalDataSource(dao: RecentPostDao): RecentPostDataSource {
        return RecentPostLocalDataSource(dao)
    }
}