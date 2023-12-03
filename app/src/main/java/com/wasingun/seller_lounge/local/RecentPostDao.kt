package com.wasingun.seller_lounge.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wasingun.seller_lounge.data.model.localpost.RecentViewedPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo

@Dao
interface RecentPostDao {

    @Query("SELECT * FROM RecentViewedPostInfo ORDER BY savedTime DESC")
    suspend fun getPostInfo(): List<RecentViewedPostInfo>

    @Query("SELECT * FROM RecentViewedPostInfo WHERE postInfo = :postInfo")
    suspend fun findPostInfo(postInfo: PostInfo): RecentViewedPostInfo?

    @Update
    suspend fun updatePostInfo(localPostInfo: RecentViewedPostInfo)

    @Insert
    suspend fun insertPostInfo(localPostInfo: RecentViewedPostInfo)

    @Delete
    suspend fun deletePostInfo(localPostInfo: RecentViewedPostInfo)
}