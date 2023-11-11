package com.wasingun.seller_lounge.data.model.post

import com.wasingun.seller_lounge.data.enums.ProductCategory

data class PostInfo(
    val postId: String,
    val category: ProductCategory,
    val title : String,
    val body: String,
    val imageList: List<String>,
    val documentList: List<String>,
    val createTime: String,
    val userId: String,
)