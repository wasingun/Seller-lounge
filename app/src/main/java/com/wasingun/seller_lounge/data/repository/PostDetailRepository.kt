package com.wasingun.seller_lounge.data.repository

import com.google.firebase.auth.FirebaseUser
import com.wasingun.seller_lounge.data.datasource.AuthFirebaseDataSource
import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.datasource.RecentViewedPostDataSource
import com.wasingun.seller_lounge.data.model.localpost.RecentViewedPostInfo
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.network.ApiResponse
import javax.inject.Inject

class PostDetailRepository @Inject constructor(
    private val postDataSource: PostDataSource,
    private val recentPostDataSource: RecentViewedPostDataSource,
    private val authDataSource: AuthFirebaseDataSource
) {
    suspend fun getWriterInfo(userId: String): ApiResponse<UserInfo> {
        return postDataSource.getWriterInfo(userId)
    }

    suspend fun findLocalPost(postId: String): RecentViewedPostInfo? {
        return recentPostDataSource.findRecentViewedPost(postId)
    }

    suspend fun saveLocalPost(localPostInfo: RecentViewedPostInfo) {
        recentPostDataSource.saveRecentViewedPost(localPostInfo)
    }

    suspend fun deletePostContent(postId: String): ApiResponse<Unit> {
        return postDataSource.deletePostContent(postId)
    }

    fun getUserInfo(): FirebaseUser? {
        return authDataSource.getCurrentUser()
    }

    suspend fun deleteRecentViewedPost(recentViewedPostInfo: RecentViewedPostInfo) {
        return recentPostDataSource.deleteRecentViewedPostList(recentViewedPostInfo)
    }
}