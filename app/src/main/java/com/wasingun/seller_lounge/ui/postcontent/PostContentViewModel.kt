package com.wasingun.seller_lounge.ui.postcontent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.DocumentContent
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostContentViewModel @Inject constructor() : ViewModel() {
    private var _imageList = MutableLiveData<List<ImageContent>>()
    val imageList: LiveData<List<ImageContent>> = _imageList
    private var _documentList = MutableLiveData<List<DocumentContent>>()
    val documentList: LiveData<List<DocumentContent>> = _documentList
    private var _isError = MutableLiveData<Event<Int>>()
    val isError: LiveData<Event<Int>> = _isError

    fun removeImageList(imageContent: ImageContent) {
        val removedList = _imageList.value?.filter {
            it.uri != imageContent.uri
        }
        _imageList.value = removedList ?: listOf()
    }

    fun removeDocumentList(documentContent: DocumentContent) {
        val removedList = _documentList.value?.filter {
            it.uri != documentContent.uri
        }
        _documentList.value = removedList ?: listOf()
    }

    fun addImageList(imageList: List<ImageContent>) {
        val currentList = _imageList.value ?: listOf()
        val newList = mutableListOf<ImageContent>()
        if (currentList.size + imageList.size > 10) {
            _isError.value = Event(R.string.announce_image_attachment_limit)
        } else {
            newList.addAll(currentList)
            newList.addAll(imageList)
            _imageList.value = newList
        }
    }

    fun addDocumentList(document: DocumentContent) {
        val currentList = _documentList.value ?: listOf()
        val newList = mutableListOf<DocumentContent>()
        if (currentList.size >= 2) {
            _isError.value = Event(R.string.announce_document_attachment_limit)
        } else {
            newList.addAll(currentList)
            newList.add(document)
            _documentList.value = newList
        }
    }
}