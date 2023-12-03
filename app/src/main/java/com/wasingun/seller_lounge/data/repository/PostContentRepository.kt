package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.data.model.attachedcontent.DocumentContent
import com.wasingun.seller_lounge.data.model.attachedcontent.ImageContent
import com.wasingun.seller_lounge.network.onError
import com.wasingun.seller_lounge.network.onException
import com.wasingun.seller_lounge.network.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostContentRepository @Inject constructor(
    private val postDataSource: PostDataSource
) {

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
        val result = postDataSource.uploadPostInfo(
            postId,
            category,
            title,
            body,
            imageList,
            documentList,
            createdTime,
            userId
        )
        return flow {
            result.onError { _, _ ->
                emit(onError(R.string.error_api_http_response))
            }.onException {
                emit(onError(R.string.error_api_network))
            }.onSuccess {
                emit(onComplete())
            }
        }
    }
}