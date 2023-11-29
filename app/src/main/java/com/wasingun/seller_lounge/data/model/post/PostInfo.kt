package com.wasingun.seller_lounge.data.model.post

import android.os.Parcelable
import com.wasingun.seller_lounge.data.model.ProductCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostInfo(
    val postId: String,
    val category: ProductCategory,
    val title : String,
    val body: String,
    val imageList: List<String>? = null,
    val documentList: List<String>? = null,
    val createTime: String,
    val userId: String,
): Parcelable