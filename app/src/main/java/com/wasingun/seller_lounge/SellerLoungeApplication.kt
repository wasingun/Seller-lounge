package com.wasingun.seller_lounge

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

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