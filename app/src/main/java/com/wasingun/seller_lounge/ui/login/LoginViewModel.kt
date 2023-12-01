package com.wasingun.seller_lounge.ui.login

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.wasingun.seller_lounge.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) :
    ViewModel() {

    fun getSignInWithCredential(authCredential: AuthCredential): Task<AuthResult> {
        return repository.getSignInWithCredential(authCredential)
    }
}