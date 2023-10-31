package com.wasingun.seller_lounge.data.enums

enum class ProductCategory(val id: String, val categoryName: String) {
    FASHION_CLOTHES("50000000", "패션의류"),
    FASHION_ACCESSORIES("50000001", "패션잡화"),
    COSMETICS_BEAUTY("50000002", "화장품/미용"),
    DIGITAL_APPLIANCES("50000003", "디지털/가전"),
    FURNITURE_INTERIOR("50000004", "가구/인테리어"),
    PARENTING("50000005", "출산/육아"),
    FOOD("50000006", "식품"),
    SPORTS_LEISURE("50000007", "스포츠/레저"),
    LIVING_HEALTH("50000008", "생활/건강"),
    LEISURE_LIFE_CONVENIENCE("50000009", "여가/생활편의");
}