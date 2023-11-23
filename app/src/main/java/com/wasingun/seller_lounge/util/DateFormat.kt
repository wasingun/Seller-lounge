package com.wasingun.seller_lounge.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
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

@BindingAdapter("convertDisplayedDate")
fun TextView.convertDisplayedDate(postedTime: String) {
    val differenceTime = System.currentTimeMillis() - postedTime.toLong()
    val differenceInSeconds = differenceTime / 1000
    val differenceInMinutes = differenceInSeconds / 60
    val differenceInHours = differenceInMinutes / 60

    text = if (differenceInSeconds < 60) {
        "$differenceInSeconds 초 전"
    } else if (differenceInMinutes < 60) {
        "$differenceInMinutes 분 전"
    } else if (differenceInHours < 24) {
        "$differenceInHours 시간 전"
    } else {
        "${differenceInHours / 24} 일 전"
    }
}