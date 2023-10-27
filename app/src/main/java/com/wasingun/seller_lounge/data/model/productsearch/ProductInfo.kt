package com.wasingun.seller_lounge.data.model.productsearch

data class ProductInfo(
    val title: String,
    val link: String,
    val image: String?,
    val lprice: String,
    val hprice: String?,
    val mallName: String,
    val productId: String,
    val productType: String,
    val brand: String?,
    val maker: String?,
    val category1: String,
    val category2: String?,
    val category3: String?,
    val category4: String?
)
