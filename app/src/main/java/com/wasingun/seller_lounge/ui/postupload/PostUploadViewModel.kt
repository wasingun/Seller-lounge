package com.wasingun.seller_lounge.ui.postupload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.data.model.attachedcontent.DocumentContent
import com.wasingun.seller_lounge.data.model.attachedcontent.ImageContent
import com.wasingun.seller_lounge.data.repository.PostUploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostUploadViewModel @Inject constructor(
    private val repository: PostUploadRepository
) : ViewModel() {
    private val _postImageList = MutableStateFlow<List<ImageContent>?>(null)
    val postImageList: StateFlow<List<ImageContent>?> = _postImageList
    private val _postDocumentList = MutableStateFlow<List<DocumentContent>?>(null)
    val postDocumentList: StateFlow<List<DocumentContent>?> = _postDocumentList
    private val _isCompleted = MutableStateFlow<Boolean>(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted
    private val _isInputError = MutableStateFlow(0)
    val isInputError: StateFlow<Int> = _isInputError
    private val _isNetworkError = MutableStateFlow(0)
    val isNetworkError: StateFlow<Int> = _isNetworkError
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val postTitle = MutableStateFlow<String?>(null)
    val postBody = MutableStateFlow<String?>(null)
    val postCategory = MutableStateFlow<String?>(null)

    fun uploadPostInfo() {
        val postId = "${System.currentTimeMillis()}"
        val category = ProductCategory.values().firstOrNull {
            postCategory.value == it.categoryName
        } ?: ProductCategory.ALL
        val title = postTitle.value ?: ""
        val body = postBody.value ?: ""
        val imageList = postImageList.value ?: listOf()
        val documentList = postDocumentList.value ?: listOf()
        val createTime = System.currentTimeMillis().toString()
        val currentUser = getCurrentUser()
        val userId = currentUser?.uid ?: ""
        if (isBlank(category, title, body)) return
        _isLoading.value = true

        viewModelScope.launch {
            repository.postInfoUploadResult(
                postId,
                category,
                title,
                body,
                imageList,
                documentList,
                createTime,
                userId,
                onComplete = {
                    _isLoading.value = false
                    _isCompleted.value = true
                },
                onError = {
                    _isNetworkError.value = it
                    _isLoading.value = false
                }
            ).collect()
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

    fun removeImageList(imageContent: ImageContent) {
        val removedList = _postImageList.value?.filter {
            it.uri != imageContent.uri
        }
        _postImageList.value = removedList ?: listOf()
    }

    fun removeDocumentList(documentContent: DocumentContent) {
        val removedList = _postDocumentList.value?.filter {
            it.uri != documentContent.uri
        }
        _postDocumentList.value = removedList ?: listOf()
    }

    fun addImageList(imageList: List<ImageContent>) {
        val currentList = _postImageList.value ?: listOf()
        val newList = mutableListOf<ImageContent>()
        if (currentList.size + imageList.size > 10) {
            _isInputError.value = R.string.announce_image_attachment_limit
        } else {
            newList.addAll(currentList)
            newList.addAll(imageList)
            _postImageList.value = newList
        }
    }

    fun addDocumentList(document: DocumentContent) {
        val currentList = _postDocumentList.value ?: listOf()
        val newList = mutableListOf<DocumentContent>()
        if (currentList.size >= 2) {
            _isInputError.value = R.string.announce_document_attachment_limit
        } else if(currentList.contains(document)) {
            _isInputError.value = R.string.announce_duplicate_file
        } else {
            newList.addAll(currentList)
            newList.add(document)
            _postDocumentList.value = newList
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return repository.getCurrentUser()
    }

    fun resetNetworkErrorState() {
        _isNetworkError.value = 0
    }

    fun resetInputError() {
        _isInputError.value = 0
    }
}