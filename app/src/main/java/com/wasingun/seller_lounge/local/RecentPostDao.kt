package com.wasingun.seller_lounge.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wasingun.seller_lounge.data.model.localpost.RecentViewedPostInfo

@Dao
interface RecentPostDao {

    @Query("SELECT * FROM RecentViewedPostInfo ORDER BY savedTime DESC")
    suspend fun getPostInfo(): List<RecentViewedPostInfo>

    @Query("SELECT * FROM RecentViewedPostInfo WHERE postId = :postId")
    suspend fun findPostInfo(postId: String): RecentViewedPostInfo?

    @Update
    suspend fun updatePostInfo(localPostInfo: RecentViewedPostInfo)

    @Insert
    suspend fun insertPostInfo(localPostInfo: RecentViewedPostInfo)

    @Delete
    suspend fun deletePostInfo(localPostInfo: RecentViewedPostInfo)
}