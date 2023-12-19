package com.wasingun.seller_lounge.ui.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.localpost.RecentViewedPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.data.repository.PostDetailRepository
import com.wasingun.seller_lounge.network.onError
import com.wasingun.seller_lounge.network.onException
import com.wasingun.seller_lounge.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(private val repository: PostDetailRepository) :
    ViewModel() {
    private val _isUserInfoLoadError = MutableStateFlow(0)
    val isUserInfoLoadError: StateFlow<Int> = _isUserInfoLoadError

    private val _isNetworkRequestError = MutableStateFlow(0)
    val isNetworkRequestError: StateFlow<Int> = _isNetworkRequestError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted

    private val _writerInfo = MutableStateFlow<UserInfo>(UserInfo("", "", "", ""))
    val writerInfo: StateFlow<UserInfo> = _writerInfo

    suspend fun getWriterInfo(userId: String) {
        val result = repository.getWriterInfo(userId)
        result.onSuccess {userInfo ->
            _writerInfo.value = userInfo
        }.onError { _, _ ->
            _isUserInfoLoadError.value = R.string.error_writer_info_load
            _isUserInfoLoadError.value = 0
        }.onException {
            _isUserInfoLoadError.value = R.string.offline_mode
            _isUserInfoLoadError.value = 0
        }
    }

    suspend fun saveLocalPost(postInfo: PostInfo) {
        val findLocalPost = repository.findLocalPost(postInfo.postId)
        val findLocalPostId = findLocalPost?.postId ?: ""
        val savedTime = System.currentTimeMillis()
        val localPostInfo = RecentViewedPostInfo(postId = postInfo.postId, postInfo = postInfo, savedTime = savedTime)
        if (findLocalPost != null && findLocalPostId == postInfo.postId) {
            repository.deleteRecentViewedPost(findLocalPost)
            repository.saveLocalPost(localPostInfo)
        } else if (findLocalPost == null) {
            repository.saveLocalPost(localPostInfo)
        }
    }

    fun getUserInfo(): FirebaseUser? {
        return repository.getUserInfo()
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val findLocalPost = repository.findLocalPost(postId)
            val result = repository.deletePost(postId)
            result.onSuccess {
                _isCompleted.value = true
            }.onError { code, message ->
                _isNetworkRequestError.value = R.string.error_api_http_response
            }.onException {
                _isNetworkRequestError.value = R.string.error_api_network
            }
            if (findLocalPost != null) {
                repository.deleteRecentViewedPost(findLocalPost)
            }
        }
    }
    fun resetNetworkRequestErrorState() {
        _isNetworkRequestError.value = 0
    }
}