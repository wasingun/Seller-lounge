package com.wasingun.seller_lounge.data.model.localpost

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wasingun.seller_lounge.data.model.post.PostInfo

@Entity
data class LocalPostInfo(
    @PrimaryKey(autoGenerate = true)
    val postId: Int = 0,
    @ColumnInfo val postInfo: PostInfo,
    @ColumnInfo val savedTime: Long,
)
