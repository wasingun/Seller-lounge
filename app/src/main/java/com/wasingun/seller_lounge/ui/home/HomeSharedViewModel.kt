package com.wasingun.seller_lounge.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeSharedViewModel @Inject constructor(): ViewModel() {

    private val _searchButtonState = MutableStateFlow(false)
    val searchButtonState:StateFlow<Boolean> = _searchButtonState
    val searchKeyword = MutableStateFlow("")

    fun searchTitle() {
        _searchButtonState.value = true
        _searchButtonState.value = false
    }
}