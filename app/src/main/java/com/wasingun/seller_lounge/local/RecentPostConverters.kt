package com.wasingun.seller_lounge.local

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.wasingun.seller_lounge.data.model.post.PostInfo

class RecentPostConverters {
    private val gson = GsonBuilder().create()
    private val adapter = gson.getAdapter(PostInfo::class.java)

    @TypeConverter
    fun convertPostInfoToJson(postInfo: PostInfo): String {
        return adapter.toJson(postInfo)
    }

    @TypeConverter
    fun convertJsonToArticle(json: String): PostInfo? {
        return adapter.fromJson(json)
    }
}