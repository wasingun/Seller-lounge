package com.wasingun.seller_lounge.ui.editpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.data.model.localpost.RecentViewedPostInfo
import com.wasingun.seller_lounge.data.repository.EditPostRepository
import com.wasingun.seller_lounge.network.onError
import com.wasingun.seller_lounge.network.onException
import com.wasingun.seller_lounge.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val repository: EditPostRepository
) : ViewModel() {
    private val _isCompleted = MutableStateFlow<Boolean>(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted
    private val _isInputError = MutableStateFlow(0)
    val isInputError: StateFlow<Int> = _isInputError
    private val _isNetworkError = MutableStateFlow(0)
    val isNetworkError: StateFlow<Int> = _isNetworkError
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun updatePost(
        postId: String,
        editedCategory: ProductCategory,
        editedTitle: String,
        editedBody: String
    ) {
        if (isBlank(editedCategory, editedTitle, editedBody)) return
        _isLoading.value = true
        viewModelScope.launch {
            repository.getPostInfo(postId).onSuccess {
                val editedPostInfo = it.copy(
                    title = editedTitle,
                    body = editedBody,
                    category = editedCategory
                )
                val result = repository.updatePost(postId, editedPostInfo)
                result.onSuccess {
                    val editedRecentViewedPostInfo = RecentViewedPostInfo(
                        postId = editedPostInfo.postId,
                        postInfo = editedPostInfo,
                        savedTime = System.currentTimeMillis()
                    )
                    val findRecentViewedPostInfo = repository.findRecentViewedPost(postId)
                    if (findRecentViewedPostInfo != null) {
                        repository.deleteRecentViewedPost(findRecentViewedPostInfo)
                        repository.saveRecentViewedPost(editedRecentViewedPostInfo)
                    }
                    _isLoading.value = false
                    _isCompleted.value = true
                }
            }.onError { code, message ->
                _isLoading.value = false
                _isNetworkError.value = R.string.error_api_http_response
            }.onException {
                _isLoading.value = false
                _isNetworkError.value = R.string.error_api_network
            }
        }
    }

    private fun isBlank(
        category: ProductCategory?,
        title: String,
        body: String
    ): Boolean {
        if (category == ProductCategory.ALL) {
            _isInputError.value = R.string.announce_blank_category
            return true
        } else if (title.isBlank()) {
            _isInputError.value = R.string.announce_input_title
            return true
        } else if (body.isBlank()) {
            _isInputError.value = R.string.announce_blank_body
            return true
        }
        return false
    }

    fun resetInputErrorState() {
        _isInputError.value = 0
    }

    fun resetNetworkErrorState() {
        _isNetworkError.value = 0
    }
}