package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.datasource.NaverApiDataSource
import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.data.model.DocumentContent
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import com.wasingun.seller_lounge.network.ApiResponse
import com.wasingun.seller_lounge.network.onError
import com.wasingun.seller_lounge.network.onException
import com.wasingun.seller_lounge.network.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class GeneralRepository @Inject constructor(
    private val naverDataSource: NaverApiDataSource,
    private val postDataSource: PostDataSource
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

    suspend fun postInfoUploadResult(
        postId: String,
        category: ProductCategory,
        title: String,
        body: String,
        imageList: List<ImageContent>,
        documentList: List<DocumentContent>,
        createdTime: String,
        userId: String,
        onComplete: () -> Unit,
        onError: (message: Int) -> Unit
    ): Flow<Unit> {
        val result = postDataSource.postInfoUpload(
            postId,
            category,
            title,
            body,
            imageList,
            documentList,
            createdTime,
            userId
        )
        return flow<Unit> {
            result.onError { _, _ ->
                onError(R.string.error_api_http_response)
            }.onException {
                onError(R.string.error_api_network)
            }
        }.onCompletion {
            onComplete()
        }
    }

    suspend fun userInfoUploadResult(userId: String, userInfo: UserInfo): ApiResponse<Unit> {
        return postDataSource.userInfoUpload(userId, userInfo)
    }
}