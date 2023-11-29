package com.wasingun.seller_lounge.di

import android.content.Context
import androidx.room.Room
import com.wasingun.seller_lounge.local.RecentPostDao
import com.wasingun.seller_lounge.local.RecentPostDatabase
import com.wasingun.seller_lounge.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context): RecentPostDatabase {
        return Room.databaseBuilder(
            context,
            RecentPostDatabase::class.java, Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideLocalPostDao(localPostDatabase: RecentPostDatabase): RecentPostDao {
        return localPostDatabase.recentPostDao()
    }
}