package com.wasingun.seller_lounge.workmanager

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.wasingun.seller_lounge.util.Constants
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
            Result.success(workDataOf(Constants.KEY_FILE_URI to uri.toString()))
        } else {
            Result.failure()
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