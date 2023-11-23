package com.wasingun.seller_lounge.data.datasource

import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wasingun.seller_lounge.data.model.localpost.LocalPostInfo

@Database(entities = [LocalPostInfo::class], version = 0)
@TypeConverters(Converters::class)
abstract class LocalPostDatabase: RoomDatabase() {
    abstract fun localPostDao(): LocalPostDao
}