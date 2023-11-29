package com.wasingun.seller_lounge.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wasingun.seller_lounge.data.model.productsearch.ProductInfo
import com.wasingun.seller_lounge.network.NaverApiClient
import javax.inject.Inject

class ShoppingProductDataSource @Inject constructor(
    private val apiClient: NaverApiClient,
    private val query: String,
    private val sort: String,
) : PagingSource<Int, ProductInfo>() {

    override fun getRefreshKey(state: PagingState<Int, ProductInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductInfo> {
        val start = params.key ?: 1
        return try {
            val data = apiClient.getSearchResult(query, start, sort)
            LoadResult.Page(
                data = data.items,
                prevKey = if(start == 1) null else start -1,
                nextKey = if(data.items.isEmpty()) null else start + params.loadSize
            )
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}