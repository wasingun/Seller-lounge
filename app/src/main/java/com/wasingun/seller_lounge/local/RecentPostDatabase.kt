package com.wasingun.seller_lounge.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wasingun.seller_lounge.data.model.localpost.RecentViewedPostInfo

@Database(entities = [RecentViewedPostInfo::class], version = 1)
@TypeConverters(RecentPostConverters::class)
abstract class RecentPostDatabase: RoomDatabase() {
    abstract fun recentPostDao(): RecentPostDao
}