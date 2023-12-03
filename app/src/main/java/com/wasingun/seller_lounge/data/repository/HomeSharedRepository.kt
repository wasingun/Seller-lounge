package com.wasingun.seller_lounge.data.repository

import com.google.firebase.auth.FirebaseUser
import com.wasingun.seller_lounge.data.datasource.AuthDataSource
import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.network.ApiResponse
import javax.inject.Inject

class HomeSharedRepository@Inject constructor(
    private val postDataSource: PostDataSource,
    private val authDataSource: AuthDataSource
) {

    suspend fun userInfoUploadResult(userId: String, userInfo: UserInfo): ApiResponse<Unit> {
        return postDataSource.uploadUserInfo(userId, userInfo)
    }

    fun getCurrentUser(): FirebaseUser? {
        return authDataSource.getCurrentUser()
    }
}