package com.wasingun.seller_lounge.ui.productsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wasingun.seller_lounge.data.model.localpost.SortSearchType
import com.wasingun.seller_lounge.data.model.productsearch.ProductInfo
import com.wasingun.seller_lounge.data.repository.ProductSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductSearchViewModel @Inject constructor(private val repository: ProductSearchRepository) :
    ViewModel() {
    val searchKeyword = MutableStateFlow<String?>(null)
    val sortType = MutableStateFlow<String?>(null)

    val item = getProductInfoList().cachedIn(viewModelScope)

    fun getProductInfoList(): Flow<PagingData<ProductInfo>> {
        val inputSearchKeyword = searchKeyword.value
        val selectedSortType = sortType.value

        val sortTypeList = SortSearchType.values()
        val sortTypeId = sortTypeList.firstOrNull {
            it.sortType == selectedSortType
        }?.id
        return repository.getProductInfoList(inputSearchKeyword ?: "", sortTypeId ?: SortSearchType.BY_RELEVANCE.id, 20)
    }
}