package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.datasource.NaverApiDataSource
import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import com.wasingun.seller_lounge.network.onError
import com.wasingun.seller_lounge.network.onException
import com.wasingun.seller_lounge.network.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrendComparisonRepository @Inject constructor(
    private val naverDataSource: NaverApiDataSource
) {

    fun requestComparisonResult(
        keywordRequest: KeywordRequest,
        onComplete: (KeywordResponse) -> Unit,
        onError: (message: Int) -> Unit,
    ): Flow<KeywordResponse> {
        return flow {
            val result = naverDataSource.requestComparisonResult(keywordRequest)
            result.onSuccess { keywordResponse ->
                onComplete(keywordResponse)
            }.onError { _, _ ->
                onError(R.string.error_api_http_response)
            }.onException {
                onError(R.string.error_api_network)
            }
        }
    }
}