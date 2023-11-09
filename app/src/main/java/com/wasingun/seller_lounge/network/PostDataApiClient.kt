package com.wasingun.seller_lounge.network

import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.model.post.UserInfo
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostDataApiClient {
    @PUT("userInfo/{userId}.json")
    suspend fun uploadUserInfo(@Path("userId") userId: String, @Body userInfo: UserInfo)

    @PUT("postInfo/{postId}.json")
    suspend fun postContent(@Path("postId") postId: String, @Body postInfo: PostInfo)
}