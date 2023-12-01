package com.wasingun.seller_lounge.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthFirebaseDataSource @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthDataSource {
    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun getSignInWithCredential(authCredential: AuthCredential): Task<AuthResult> {
        return firebaseAuth.signInWithCredential(authCredential)
    }
}