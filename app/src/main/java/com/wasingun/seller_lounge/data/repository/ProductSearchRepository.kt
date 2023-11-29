package com.wasingun.seller_lounge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wasingun.seller_lounge.data.datasource.ShoppingProductDataSource
import com.wasingun.seller_lounge.network.NaverApiClient
import javax.inject.Inject

class ProductSearchRepository @Inject constructor(
    private val naverApiClient: NaverApiClient
) {

    fun getProductInfoList(query: String, sort: String, pageSize: Int) = Pager(
        PagingConfig(
            pageSize = pageSize,
        )
    ) {
        ShoppingProductDataSource(
            naverApiClient,
            query,
            sort
        )
    }.flow
}