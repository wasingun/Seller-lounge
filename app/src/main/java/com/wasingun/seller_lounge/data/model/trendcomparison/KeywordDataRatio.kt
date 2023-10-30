package com.wasingun.seller_lounge.data.model.trendcomparison

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KeywordDataRatio(
    val period: String,
    val ratio: Double
): Parcelable
