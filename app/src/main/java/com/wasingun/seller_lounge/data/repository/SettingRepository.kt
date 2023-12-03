package com.wasingun.seller_lounge.data.repository

import com.google.firebase.auth.FirebaseUser
import com.wasingun.seller_lounge.data.datasource.AuthDataSource
import javax.inject.Inject

class SettingRepository @Inject constructor(private val authDataSource: AuthDataSource) {

    fun getCurrentUser(): FirebaseUser? {
        return authDataSource.getCurrentUser()
    }
}