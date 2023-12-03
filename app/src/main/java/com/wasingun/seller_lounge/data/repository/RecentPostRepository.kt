package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.data.datasource.RecentViewedPostDataSource
import com.wasingun.seller_lounge.data.model.post.PostInfo
import javax.inject.Inject

class RecentPostRepository @Inject constructor(
    private val postDataSource: RecentViewedPostDataSource
) {

    suspend fun getLocalPostList(): List<PostInfo> {
        return postDataSource.getRecentViewedPostList()
    }
}