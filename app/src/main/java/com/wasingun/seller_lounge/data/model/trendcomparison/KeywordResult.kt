package com.wasingun.seller_lounge.data.model.trendcomparison

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KeywordResult(
    val title: String,
    val keyword: List<String>,
    val data: List<KeywordDataRatio>
): Parcelable