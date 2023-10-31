package com.wasingun.seller_lounge.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun latestDateFormat(date: Long): String {
    val oneDaysInMills = 48L * 60 * 60 * 1000
    val oneDayAgo = date - oneDaysInMills
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date(oneDayAgo))
}

fun thirtyDaysAgoDateFormat(date: Long): String {
    val thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000
    val thirtyDaysAgo = date - thirtyDaysInMillis
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date(thirtyDaysAgo))
}

fun removeYear(date: String): String {
    return date.substring(5 until 10)
}