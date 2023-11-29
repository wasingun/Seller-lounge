package com.wasingun.seller_lounge.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wasingun.seller_lounge.data.model.localpost.LocalPostConverters
import com.wasingun.seller_lounge.data.model.localpost.LocalPostInfo

@Database(entities = [LocalPostInfo::class], version = 1)
@TypeConverters(LocalPostConverters::class)
abstract class LocalPostDatabase: RoomDatabase() {
    abstract fun localPostDao(): LocalPostDao
}