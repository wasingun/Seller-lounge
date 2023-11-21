package com.wasingun.seller_lounge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.repository.GeneralRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomePostViewModel @Inject constructor(private val repository: GeneralRepository) :
    ViewModel() {

    val postList: StateFlow<List<PostInfo>> = getPost().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(10000),
        initialValue = listOf()
    )
    private val _isError = MutableStateFlow(0)
    val isError: StateFlow<Int> = _isError
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getPost(): Flow<List<PostInfo>> {
        val result = repository.getPostList(
            onComplete = {
                _isLoading.value = false
            },
            onError = { _isError.value = it }
        ).map { postList ->
            postList.sortedByDescending { it.createTime }
        }
        return result
    }
}