package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.data.model.localpost.RecentViewedPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo

interface RecentViewedPostDataSource {

    suspend fun saveRecentViewedPost(localPostInfo: RecentViewedPostInfo)

    suspend fun findRecentViewedPost(postInfo: PostInfo): RecentViewedPostInfo?

    suspend fun getRecentViewedPostList(): List<PostInfo>
}