package com.wasingun.seller_lounge.extensions

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.wasingun.seller_lounge.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun View.showTextMessage(@StringRes messageId: Int) {
    try {
        Snackbar.make(this, messageId, Snackbar.LENGTH_SHORT).show()
    } catch (e: Exception) {

    }
}

fun View.setClickEvent(
    uiScope: CoroutineScope,
    windowDuration: Long = Constants.THROTTLE_DURATION,
    onClick: () -> Unit,
) {
    clicks()
        .throttleFirst(windowDuration)
        .onEach { onClick.invoke() }
        .launchIn(uiScope)
}

fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        this.trySend(Unit)
    }
    awaitClose { setOnClickListener(null) }
}