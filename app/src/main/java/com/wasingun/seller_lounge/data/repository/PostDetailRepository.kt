package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.datasource.RecentPostDataSource
import com.wasingun.seller_lounge.data.model.localpost.LocalPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.network.ApiResponse
import javax.inject.Inject

class PostDetailRepository @Inject constructor(
    private val postDataSource: PostDataSource,
    private val recentPostDataSource: RecentPostDataSource
) {
    suspend fun getWriterInfo(userId: String): ApiResponse<UserInfo> {
        return postDataSource.getWriterInfo(userId)
    }

    suspend fun findLocalPost(postInfo: PostInfo): LocalPostInfo? {
        return recentPostDataSource.findLocalPost(postInfo)
    }

    suspend fun saveLocalPost(localPostInfo: LocalPostInfo) {
        recentPostDataSource.saveLocalPost(localPostInfo)
    }
}