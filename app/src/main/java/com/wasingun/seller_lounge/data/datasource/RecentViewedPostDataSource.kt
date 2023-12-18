package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.data.model.localpost.RecentViewedPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo

interface RecentViewedPostDataSource {

    suspend fun saveRecentViewedPost(localPostInfo: RecentViewedPostInfo)

    suspend fun findRecentViewedPost(postId: String): RecentViewedPostInfo?

    suspend fun getRecentViewedPostList(): List<PostInfo>

    suspend fun deleteRecentViewedPostList(localPostInfo: RecentViewedPostInfo)
}