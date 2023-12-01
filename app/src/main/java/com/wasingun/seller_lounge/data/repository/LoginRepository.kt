package com.wasingun.seller_lounge.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.wasingun.seller_lounge.data.datasource.AuthDataSource
import javax.inject.Inject

class LoginRepository @Inject constructor(private val authDataSource: AuthDataSource){

    fun getSignInWithCredential(authCredential: AuthCredential): Task<AuthResult> {
        return authDataSource.getSignInWithCredential(authCredential)
    }
}