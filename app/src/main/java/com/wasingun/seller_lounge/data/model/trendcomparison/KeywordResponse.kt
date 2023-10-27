package com.wasingun.seller_lounge.data.model.trendcomparison

data class KeywordResponse(
    val startDate: String,
    val endDate: String,
    val timeUnit: String,
    val results: List<KeywordResult>
)
