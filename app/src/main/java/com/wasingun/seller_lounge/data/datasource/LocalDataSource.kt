package com.wasingun.seller_lounge.data.datasource

import com.wasingun.seller_lounge.data.model.localpost.LocalPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo

interface LocalDataSource {

    suspend fun saveLocalPost(localPostInfo: LocalPostInfo)

    suspend fun findLocalPost(postInfo: PostInfo): LocalPostInfo?

    suspend fun getLocalPostList(): List<PostInfo>
}