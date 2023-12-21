package com.wasingun.seller_lounge

import android.app.Application
import android.os.Build
import com.wasingun.seller_lounge.data.preferencemanager.PreferenceManager
import com.wasingun.seller_lounge.notification.NotificationCompatManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SellerLoungeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompatManager(this).createChannel()
        }
        preferenceManager = PreferenceManager(this)
    }

    companion object {
        lateinit var preferenceManager: PreferenceManager
    }
}