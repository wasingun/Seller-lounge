package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import com.wasingun.seller_lounge.network.ApiResponse
import javax.inject.Inject

class SearchContentRepository @Inject constructor(private val dataSource: SearchContentDataSource) {

    suspend fun requestComparisonResult(keywordRequest: KeywordRequest): ApiResponse<KeywordResponse> {
        return dataSource.requestComparisonResult(keywordRequest)
    }
}