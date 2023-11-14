package com.wasingun.seller_lounge.ui.postcontent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.SellerLoungeApplication
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.data.model.DocumentContent
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.data.repository.GeneralRepository
import com.wasingun.seller_lounge.network.onError
import com.wasingun.seller_lounge.network.onException
import com.wasingun.seller_lounge.network.onSuccess
import com.wasingun.seller_lounge.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostContentViewModel @Inject constructor(private val repository: GeneralRepository) :
    ViewModel() {
    private var _postImageList = MutableLiveData<List<ImageContent>>()
    val postImageList: LiveData<List<ImageContent>> = _postImageList
    private var _postDocumentList = MutableLiveData<List<DocumentContent>>()
    val postDocumentList: LiveData<List<DocumentContent>> = _postDocumentList
    private val _isCompleted = MutableLiveData<Boolean>(false)
    val isCompleted: LiveData<Boolean> = _isCompleted
    private var _isError = MutableLiveData<Event<Int>>()
    val isError: LiveData<Event<Int>> = _isError

    val postTitle = MutableLiveData<String>()
    val postBody = MutableLiveData<String>()
    val postCategory = MutableLiveData<String>()

    fun uploadPostInfo() {
        val postId = "${System.currentTimeMillis()}"
        val category = ProductCategory.values().firstOrNull {
            postCategory.value == it.categoryName
        } ?: ProductCategory.NONE
        val title = postTitle.value ?: ""
        val body = postBody.value ?: ""
        val imageList = postImageList.value ?: listOf()
        val documentList = postDocumentList.value ?: listOf()
        val createTime = System.currentTimeMillis().toString()
        val userId = SellerLoungeApplication.auth.currentUser?.uid ?: ""

        if (isBlank(category, title, body)) return

        viewModelScope.launch {
            val result = repository.postInfoUploadResult(
                postId,
                category,
                title,
                body,
                imageList,
                documentList,
                createTime,
                userId
            )
            result.onSuccess {
                _isCompleted.value = true
            }.onError { _, _ ->
                _isError.value = Event(R.string.error_api_http_response)
            }.onException {
                _isError.value = Event(R.string.error_api_network)
            }
        }
    }

    private fun isBlank(
        category: ProductCategory?,
        title: String,
        body: String
    ): Boolean {
        if (category == ProductCategory.NONE) {
            _isError.value = Event(R.string.announce_blank_category)
            return true
        } else if (title.isBlank()) {
            _isError.value = Event(R.string.announce_input_title)
            return true
        } else if (body.isBlank()) {
            _isError.value = Event(R.string.announce_blank_body)
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
            _isError.value = Event(R.string.announce_image_attachment_limit)
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
            _isError.value = Event(R.string.announce_document_attachment_limit)
        } else {
            newList.addAll(currentList)
            newList.add(document)
            _postDocumentList.value = newList
        }
    }
}