package com.wasingun.seller_lounge.ui.setting

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.wasingun.seller_lounge.data.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val authRepository: SettingRepository) :
    ViewModel() {

    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }
}