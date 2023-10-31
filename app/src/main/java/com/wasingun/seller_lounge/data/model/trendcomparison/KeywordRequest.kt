package com.wasingun.seller_lounge.data.model.trendcomparison

data class KeywordRequest(
    val startDate: String,
    val endDate: String,
    val timeUnit: String,
    val category: String,
    val keyword: List<KeywordDetail>
)
