package com.wasingun.seller_lounge.workmanager

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.constants.Constants
import com.wasingun.seller_lounge.constants.NotificationConstants
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class CoroutineDownloadWorker(private val context: Context, private val params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val documentUrl = inputData.getString(Constants.KEY_DOCUMENT_URL) ?: ""
        val fileName = inputData.getString(Constants.KEY_FILE_NAME) ?: ""
        if (documentUrl.isEmpty() || fileName.isEmpty()) {
            return Result.failure()
        }
        val uri = getFileUri(
            fileName = fileName, url = documentUrl, context = context
        )
        return if (uri != null) {
            setNotification(
                NotificationConstants.CHANNEL_ID_DOCUMENT_DOWNLOAD,
                NotificationConstants.CHANNEL_ID_INITIAL_DOCUMENT_DOWNLOAD,
                context.getString(R.string.notification_document_download_title),
                context.getString(R.string.notification_document_download_ticker),
                context.getString(R.string.notification_document_download_content),
                Build.VERSION_CODES.TIRAMISU,
                Manifest.permission.POST_NOTIFICATIONS,
            )
            Result.success(workDataOf(Constants.KEY_FILE_URI to uri.toString()))
        } else {
            Result.failure()
        }
    }

    private fun setNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        ticker: String,
        contentText: String,
        permissionBuildVersion: Int,
        permissionType: String
    ) {
        val notification =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setTicker(ticker)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setOngoing(false)
                .build()

        if (Build.VERSION.SDK_INT >= permissionBuildVersion) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permissionType
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context)
                    .notify(
                        notificationId,
                        notification
                    )
            }
        } else {
            NotificationManagerCompat.from(context)
                .notify(notificationId, notification)
        }
    }

    private fun getFileUri(fileName: String, url: String, context: Context): Uri? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val mimeType = when (fileName.substringAfterLast('.', "")) {
                "pdf" -> "application/pdf"
                "zip" -> "application/zip"
                "doc" -> "application/msword"
                "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                "xls" -> "application/vnd.ms-excel"
                "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                "ppt" -> "application/vnd.ms-powerpoint"
                else -> {
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                }
            }
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                )
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                contentValues
            )
            return if (uri != null) {
                URL(url).openStream().use { input ->
                    resolver.openOutputStream(uri)?.use { output ->
                        input.copyTo(output)
                    }
                }
                uri
            } else {
                null
            }
        } else {
            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            URL(url).openStream().use { input ->
                FileOutputStream(target).use { output ->
                    input.copyTo(output)
                }
            }
            return target.toUri()
        }
    }
}