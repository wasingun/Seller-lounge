package com.wasingun.seller_lounge.ui.productsearch

import com.wasingun.seller_lounge.data.model.productsearch.ProductInfo

fun interface ProductClickListener {

    fun clickProductInfo(productInfo: ProductInfo)
}