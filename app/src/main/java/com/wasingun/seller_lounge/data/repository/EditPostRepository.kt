package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.datasource.RecentViewedPostDataSource
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.network.ApiResponse
import javax.inject.Inject

class EditPostRepository @Inject constructor(
    private val postDataSource: PostDataSource,
    private val recentViewedPostDataSource: RecentViewedPostDataSource) {

    suspend fun updatePostContent(postId: String, postInfo: PostInfo): ApiResponse<Unit> {
        return postDataSource.updatePostContent(postId, postInfo)
    }

    suspend fun getPostInfo(postId:String): ApiResponse<PostInfo> {
        return postDataSource.getPostInfo(postId)
    }
}