package com.wasingun.seller_lounge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.data.repository.HomeSharedRepository
import com.wasingun.seller_lounge.network.onError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeSharedViewModel @Inject constructor(
    private val userRepository: HomeSharedRepository
) :
    ViewModel() {
    private val _isError = MutableStateFlow(0)
    val isError: StateFlow<Int> = _isError
    val searchKeyword = MutableStateFlow("")

    private val currentUser = getCurrentUser()

    fun updateUserInfo() {
        viewModelScope.launch {
            val userId = currentUser?.uid ?: "None"
            val userName = currentUser?.displayName ?: ""
            val userEmail = currentUser?.email ?: ""
            val userImage = currentUser?.photoUrl.toString()
            val result = userRepository.userInfoUploadResult(
                userId, UserInfo(userId, userName, userEmail, userImage)
            )
            result.onError { code, message ->
                _isError.value = R.string.error_user_info_update
                _isError.value = 0
            }
        }
    }

    fun getCurrentUser() = userRepository.getCurrentUser()
}