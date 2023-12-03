package com.wasingun.seller_lounge.di

import com.wasingun.seller_lounge.data.datasource.RecentViewedPostDataSource
import com.wasingun.seller_lounge.data.datasource.RecentViewedPostLocalDataSource
import com.wasingun.seller_lounge.local.RecentPostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RecentPostDataSourceModule {

    @Provides
    fun provideLocalDataSource(dao: RecentPostDao): RecentViewedPostDataSource {
        return RecentViewedPostLocalDataSource(dao)
    }
}