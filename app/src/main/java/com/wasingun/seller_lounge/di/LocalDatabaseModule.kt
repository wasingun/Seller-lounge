package com.wasingun.seller_lounge.di

import com.wasingun.seller_lounge.SellerLoungeApplication
import com.wasingun.seller_lounge.local.LocalPostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseModule {

    @Singleton
    @Provides
    fun provideLocalDatabase():LocalPostDao {
        return SellerLoungeApplication.db.localPostDao()
    }
}