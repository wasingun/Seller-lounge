package com.wasingun.seller_lounge.data.model.localpost

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wasingun.seller_lounge.data.model.post.PostInfo

@Entity
data class RecentViewedPostInfo(
    @PrimaryKey(autoGenerate = true)
    val recentViewedPostId: Int = 0,
    @ColumnInfo val postId: String,
    @ColumnInfo val postInfo: PostInfo,
    @ColumnInfo val savedTime: Long,
)