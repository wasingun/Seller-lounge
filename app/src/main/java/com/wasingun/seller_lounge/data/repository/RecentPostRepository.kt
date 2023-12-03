package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.datasource.RecentPostDataSource
import com.wasingun.seller_lounge.data.model.post.PostInfo
import javax.inject.Inject

class RecentPostRepository @Inject constructor(
    private val postDataSource: RecentPostDataSource
){

    suspend fun getLocalPostList(): List<PostInfo> {
        return postDataSource.getLocalPostList()
    }
}