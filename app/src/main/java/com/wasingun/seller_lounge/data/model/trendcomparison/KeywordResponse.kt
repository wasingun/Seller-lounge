package com.wasingun.seller_lounge.data.model.trendcomparison

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KeywordResponse(
    val startDate: String,
    val endDate: String,
    val timeUnit: String,
    val results: List<KeywordResult>
): Parcelable
