package com.wasingun.seller_lounge

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

class SellerLoungeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        auth = FirebaseAuth.getInstance()
    }

    companion object {
        lateinit var auth: FirebaseAuth
    }
}