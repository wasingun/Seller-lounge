package com.wasingun.seller_lounge

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SellerLoungeApplication : Application() {

    override fun onCreate() {
        appContainer = AppContainer()

        super.onCreate()
        auth = FirebaseAuth.getInstance()
    }

    companion object {
        lateinit var auth: FirebaseAuth
        lateinit var appContainer: AppContainer
    }
}