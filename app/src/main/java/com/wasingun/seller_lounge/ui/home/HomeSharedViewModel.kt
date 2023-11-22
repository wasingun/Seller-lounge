package com.wasingun.seller_lounge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.SellerLoungeApplication
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.data.repository.GeneralRepository
import com.wasingun.seller_lounge.network.onError
import com.wasingun.seller_lounge.network.onException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeSharedViewModel @Inject constructor(private val repository: GeneralRepository) :
    ViewModel() {

    private val _isError = MutableStateFlow(0)
    val isError: StateFlow<Int> = _isError
    private val _searchButtonState = MutableStateFlow(false)
    val searchButtonState: StateFlow<Boolean> = _searchButtonState

    val searchKeyword = MutableStateFlow("")

    fun searchTitle() {
        _searchButtonState.value = true
        _searchButtonState.value = false
    }

    fun updateUserInfo() {
        viewModelScope.launch {
            val currentUser = SellerLoungeApplication.auth.currentUser
            val userId = currentUser?.uid ?: ""
            val userName = currentUser?.displayName ?: ""
            val userEmail = currentUser?.email ?: ""
            val userImage = currentUser?.photoUrl.toString() ?: ""
            val result = repository.userInfoUploadResult(
                userId, UserInfo(userId, userName, userEmail, userImage)
            )
            result.onError { code, message ->
                _isError.value = R.string.error_user_info_update
                _isError.value = 0
            }.onException {
                _isError.value = R.string.error_api_network
                _isError.value = 0
            }
        }
    }
}