package com.wasingun.seller_lounge.data.preferencemanager

import android.content.Context
import com.wasingun.seller_lounge.constants.Constants

class PreferenceManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        Constants.KEY_SHARED_PREFERENCE,
        Context.MODE_PRIVATE
    )

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, value)
            apply()
        }
    }
}