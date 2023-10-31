package com.wasingun.seller_lounge.data.model.productsearch

data class SearchResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<ProductInfo>
)
