package com.wasingun.seller_lounge.data.preferencemanager

import com.wasingun.seller_lounge.SellerLoungeApplication
import com.wasingun.seller_lounge.constants.Constants

object DataStore {
    val permissionCheck get() = SellerLoungeApplication.preferenceManager.getBoolean(Constants.KEY_PERMISSION_CHECK, false)
}