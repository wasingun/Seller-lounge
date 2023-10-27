package com.wasingun.seller_lounge.data.model.trendcomparison

data class KeywordResult(
    val title: String,
    val keyword: List<String>,
    val data: List<KeywordDataRatio>
)