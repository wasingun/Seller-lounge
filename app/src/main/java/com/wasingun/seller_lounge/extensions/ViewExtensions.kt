package com.wasingun.seller_lounge.extensions

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showTextMessage(@StringRes messageId: Int) {
    Snackbar.make(this, messageId, Snackbar.LENGTH_SHORT).show()
}