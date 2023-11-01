package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import com.wasingun.seller_lounge.data.source.NaverApiClient
import javax.inject.Inject

class TrendComparisonRepository @Inject constructor(private val naverApiClient: NaverApiClient) {

    suspend fun requestComparisonResult(keywordRequest: KeywordRequest): KeywordResponse {
        return naverApiClient.getComparedKeywordData(keywordRequest)
    }
}