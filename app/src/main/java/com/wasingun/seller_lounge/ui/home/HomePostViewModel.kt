package com.wasingun.seller_lounge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.repository.PostListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePostViewModel @Inject constructor(private val repository: PostListRepository) :
    ViewModel() {
    val remotePostList: StateFlow<List<PostInfo>> = getRemotePostList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(10000),
        initialValue = listOf(),
    )

    private val _localPostList = MutableStateFlow(listOf<PostInfo>())
    val localPostList: StateFlow<List<PostInfo>> = _localPostList

    private val _isError = MutableStateFlow(0)
    val isError: StateFlow<Int> = _isError

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getRemotePostList(): Flow<List<PostInfo>> {
        val result = repository.getRemotePostList(
            onComplete = {
                _isLoading.value = false
            },
            onError = {errorMessage ->
                _isError.value = errorMessage
                _isError.value = 0
                _isLoading.value = false
            }
        ).map { postList ->
            postList.sortedByDescending { it.createTime }
        }
        return result
    }

    fun getRecentViewedPostList() {
        _isLoading.value = false
        viewModelScope.launch {
            val result = repository.getRecentViewedPostList()
            _localPostList.value = result
        }
    }
}