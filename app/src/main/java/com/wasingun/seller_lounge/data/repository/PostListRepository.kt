package com.wasingun.seller_lounge.data.repository

import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.datasource.PostDataSource
import com.wasingun.seller_lounge.data.datasource.RecentViewedPostDataSource
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostListRepository @Inject constructor(
    private val postDataSource: PostDataSource,
    private val recentPostDataSource: RecentViewedPostDataSource
) {

    fun getRemotePostList(
        onComplete: () -> Unit,
        onError: (Int) -> Unit
    ): Flow<List<PostInfo>> {

        val result = postDataSource.getPostList(
            onError = {
                if (it == Constants.REQUEST_ERROR) {
                    onError(R.string.error_api_http_response)
                } else if (it == Constants.NETWORK_ERROR) {
                    onError(R.string.error_api_network)
                }
            }, onComplete = onComplete
        )
        return result
    }

    suspend fun getRecentViewedPostList(): List<PostInfo> {
        return recentPostDataSource.getRecentViewedPostList()
    }
}