package com.wasingun.seller_lounge.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wasingun.seller_lounge.data.model.localpost.LocalPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo

@Dao
interface RecentPostDao {

    @Query("SELECT * FROM LocalPostInfo ORDER BY savedTime DESC")
    suspend fun getPostInfo(): List<LocalPostInfo>

    @Query("SELECT * FROM LocalPostInfo WHERE postInfo = :postInfo")
    suspend fun findPostInfo(postInfo: PostInfo): LocalPostInfo?

    @Update
    suspend fun updatePostInfo(localPostInfo: LocalPostInfo)

    @Insert
    suspend fun insertPostInfo(localPostInfo: LocalPostInfo)

    @Delete
    suspend fun deletePostInfo(localPostInfo: LocalPostInfo)
}