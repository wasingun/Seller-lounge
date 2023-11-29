package com.wasingun.seller_lounge.data.enums

enum class SortSearchType(val id: String, val sortType: String) {
    BY_RELEVANCE("sim", "정확도순"),
    BY_PRICE_ASCENDING("asc", "낮은 가격순"),
    BY_PRICE_DESCENDING("dsc", "높은 가격순")
}