package com.wasingun.seller_lounge

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.wasingun.seller_lounge.local.LocalPostDatabase
import com.wasingun.seller_lounge.util.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SellerLoungeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        auth = FirebaseAuth.getInstance()
        db = Room.databaseBuilder(
            applicationContext,
            LocalPostDatabase::class.java, Constants.DATABASE_NAME
        ).build()
    }

    companion object {
        lateinit var auth: FirebaseAuth
        lateinit var db: LocalPostDatabase
    }
}