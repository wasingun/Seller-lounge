package com.wasingun.seller_lounge.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wasingun.seller_lounge.data.model.localpost.LocalPostInfo
import com.wasingun.seller_lounge.data.model.post.PostInfo

@Dao
interface LocalPostDao {

    @Query("SELECT * FROM LocalPostInfo ORDER BY savedTime DESC")
    fun getPostInfo(): List<LocalPostInfo>

    @Query("SELECT * FROM LocalPostInfo WHERE postInfo = :postInfo")
    fun findPostInfo(postInfo: PostInfo): LocalPostInfo

    @Update
    fun updatePostInfo(localPostInfo: LocalPostInfo)

    @Insert
    fun insertPostInfo(localPostInfo: LocalPostInfo)

    @Delete
    fun deletePostInfo(localPostInfo: LocalPostInfo)
}