package com.wasingun.seller_lounge.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthDataSource {

    fun getCurrentUser(): FirebaseUser?

    fun getSignInWithCredential(authCredential: AuthCredential): Task<AuthResult>
}