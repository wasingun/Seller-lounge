package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.data.datasource.NaverApiDataSource
import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.data.model.DocumentContent
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import com.wasingun.seller_lounge.network.ApiResponse
import javax.inject.Inject

class GeneralRepository @Inject constructor(
    private val naverDataSource: NaverApiDataSource,
    private val postDataSource: PostDataSource
) {

    suspend fun requestComparisonResult(keywordRequest: KeywordRequest): ApiResponse<KeywordResponse> {
        return naverDataSource.requestComparisonResult(keywordRequest)
    }

    suspend fun postInfoUploadResult(
        postId: String,
        category: ProductCategory,
        title: String,
        body: String,
        imageList: List<ImageContent>,
        documentList: List<DocumentContent>,
        createdTime: String,
        userId: String
    ): ApiResponse<Unit> {
        return postDataSource.postInfoUpload(
            postId,
            category,
            title,
            body,
            imageList,
            documentList,
            createdTime,
            userId
        )
    }

    suspend fun userInfoUploadResult(userId: String, userInfo: UserInfo): ApiResponse<Unit> {
        return postDataSource.userInfoUpload(userId, userInfo)
    }
}