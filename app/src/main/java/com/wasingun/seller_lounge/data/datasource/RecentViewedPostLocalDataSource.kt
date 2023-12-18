package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.data.model.localpost.RecentViewedPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.local.RecentPostDao
import javax.inject.Inject

class RecentViewedPostLocalDataSource @Inject constructor(private val dao: RecentPostDao) : RecentViewedPostDataSource {
    override suspend fun saveRecentViewedPost(localPostInfo: RecentViewedPostInfo) {
        dao.insertPostInfo(localPostInfo)
    }

    override suspend fun findRecentViewedPost(postId: String): RecentViewedPostInfo? {
        return dao.findPostInfo(postId)
    }

    override suspend fun getRecentViewedPostList(): List<PostInfo> {
        val result = dao.getPostInfo()
        for (localPostInfo in result) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - localPostInfo.savedTime > 172800000) {
                dao.deletePostInfo(localPostInfo)
            }
        }
        return dao.getPostInfo().map { localPostInfo ->
            localPostInfo.postInfo
        }
    }

    override suspend fun deleteRecentViewedPostList(localPostInfo: RecentViewedPostInfo) {
        dao.deletePostInfo(localPostInfo)
    }
}