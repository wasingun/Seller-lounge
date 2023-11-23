package com.wasingun.seller_lounge.util

import android.os.Build
import android.os.Bundle
import java.io.Serializable

fun <T : Serializable> Bundle.getSerializableCompat(key: String, clazz: Class<T>? = null): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && clazz != null) {
        getSerializable(key, clazz)
    } else {
        getSerializable(key) as? T
    }
}