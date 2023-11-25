package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.data.model.localpost.LocalPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.local.LocalPostDao
import javax.inject.Inject

class LocalDataPostSource @Inject constructor(private val dao: LocalPostDao) : LocalDataSource {
    override suspend fun saveLocalPost(localPostInfo: LocalPostInfo) {
        dao.insertPostInfo(localPostInfo)
    }

    override suspend fun findLocalPost(postInfo: PostInfo): LocalPostInfo? {
        return dao.findPostInfo(postInfo)
    }

    override suspend fun getLocalPostList(): List<PostInfo> {
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
}