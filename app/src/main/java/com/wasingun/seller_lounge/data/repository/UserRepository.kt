package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.network.ApiResponse
import javax.inject.Inject

class UserRepository@Inject constructor(
    private val postDataSource: PostDataSource
) {

    suspend fun userInfoUploadResult(userId: String, userInfo: UserInfo): ApiResponse<Unit> {
        return postDataSource.uploadUserInfo(userId, userInfo)
    }
}