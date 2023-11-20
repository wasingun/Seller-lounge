package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.data.model.DocumentContent
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.network.ApiResponse

interface PostDataSource {

    suspend fun postInfoUpload(postId: String,
                               category: ProductCategory,
                               title: String,
                               body: String,
                               imageList: List<ImageContent>,
                               documentList: List<DocumentContent>,
                               createdTime: String,
                               userId: String): ApiResponse<Unit>

    suspend fun userInfoUpload(userId: String, userInfo: UserInfo): ApiResponse<Unit>
}