package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import com.wasingun.seller_lounge.data.repository.SearchContentDataSource
import com.wasingun.seller_lounge.network.ApiResponse
import com.wasingun.seller_lounge.network.NaverApiClient
import javax.inject.Inject

class SearchContentRemoteDataSource @Inject constructor(private val naverApiClient: NaverApiClient):
    SearchContentDataSource {

    override suspend fun requestComparisonResult(keywordRequest: KeywordRequest): ApiResponse<KeywordResponse> {
        return naverApiClient.getComparedKeywordData(keywordRequest)
    }
}