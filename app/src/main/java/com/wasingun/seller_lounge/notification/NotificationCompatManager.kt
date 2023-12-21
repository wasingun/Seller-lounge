package com.wasingun.seller_lounge.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.constants.NotificationConstants

class NotificationCompatManager(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel() {
        val name = context.getString(R.string.notification_channel_name_document_download)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val documentDownloadChannel = NotificationChannel(
            NotificationConstants.CHANNEL_ID_DOCUMENT_DOWNLOAD,
            name,
            importance
        ).apply {
            description = R.string.document_download_channel_description.toString()
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(
            listOf(
                documentDownloadChannel
            )
        )
    }
}