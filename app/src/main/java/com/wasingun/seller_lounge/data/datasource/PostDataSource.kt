package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.data.model.attachedcontent.DocumentContent
import com.wasingun.seller_lounge.data.model.attachedcontent.ImageContent
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface PostDataSource {

    fun getPostList(
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ): Flow<List<PostInfo>>

    suspend fun uploadPostInfo(
        postId: String,
        category: ProductCategory,
        title: String,
        body: String,
        imageList: List<ImageContent>,
        documentList: List<DocumentContent>,
        createdTime: String,
        userId: String
    ): ApiResponse<Unit>

    suspend fun uploadUserInfo(userId: String, userInfo: UserInfo): ApiResponse<Unit>

    suspend fun getWriterInfo(userId: String):ApiResponse<UserInfo>

    suspend fun deletePostContent(postId: String): ApiResponse<Unit>
}