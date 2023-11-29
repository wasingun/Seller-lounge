package com.wasingun.seller_lounge.ui.productsearch

import androidx.lifecycle.ViewModel
import com.wasingun.seller_lounge.data.repository.GeneralRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductSearchViewModel @Inject constructor(private val repository: GeneralRepository) :
    ViewModel() {

    val searchKeyword = MutableStateFlow<String?>("")
    val sortType = MutableStateFlow<String?>("")
}