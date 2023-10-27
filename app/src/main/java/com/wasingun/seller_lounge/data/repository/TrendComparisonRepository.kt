package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.source.NaverApiClient

class TrendComparisonRepository(private val naverApiClient: NaverApiClient) {

    suspend fun requestComparisonResult(keywordRequest: KeywordRequest) {
        naverApiClient.getComparedKeywordData(keywordRequest)
    }
}