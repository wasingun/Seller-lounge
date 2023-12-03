package com.wasingun.seller_lounge.ui.postdetail

import androidx.lifecycle.ViewModel
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
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(private val repository: PostDetailRepository) :
    ViewModel() {
    private val _isError = MutableStateFlow(0)
    val isError: StateFlow<Int> = _isError

    private val _writerInfo = MutableStateFlow<UserInfo>(UserInfo("", "", "", ""))
    val writerInfo: StateFlow<UserInfo> = _writerInfo

    suspend fun getWriterInfo(userId: String) {
        val result = repository.getWriterInfo(userId)
        result.onSuccess {
            _writerInfo.value = it
        }.onError { _, _ ->
            _isError.value = R.string.error_writer_info_load
            _isError.value = 0
        }.onException {
            _isError.value = R.string.offline_mode
            _isError.value = 0
        }
    }

    suspend fun saveLocalPost(postInfo: PostInfo) {
        val findLocalPost = repository.findLocalPost(postInfo)
        val savedTime = System.currentTimeMillis()
        val localPostInfo = RecentViewedPostInfo(postInfo = postInfo, savedTime = savedTime)
        if (findLocalPost == null) {
            repository.saveLocalPost(localPostInfo)
        }
    }
}