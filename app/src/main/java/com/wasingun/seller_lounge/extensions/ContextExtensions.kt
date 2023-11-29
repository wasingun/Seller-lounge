package com.wasingun.seller_lounge.extensions

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.Size
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

fun Context.setDialogSize(
    dialogFragment: DialogFragment,
    widthRatio: Float,
    heightRatio: Float
) {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val window = dialogFragment.dialog?.window
    fun calculateSize(width: Int, height: Int, widthRatio: Float, heightRatio: Float): Size {
        val isLandScape = width >= height
        return if (isLandScape) {
            Size((width * 0.6).toInt(), (height * 0.5).toInt())
        } else {
            Size((width * widthRatio).toInt(), (height * heightRatio).toInt())
        }
    }

    val newSize = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        calculateSize(size.x, size.y, widthRatio, heightRatio)
    } else {
        val rect = windowManager.currentWindowMetrics.bounds
        calculateSize(rect.width(), rect.height(), widthRatio, heightRatio)
    }
    window?.setLayout(newSize.width, newSize.height)
}