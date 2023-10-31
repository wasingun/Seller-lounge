package com.wasingun.seller_lounge

import com.wasingun.seller_lounge.data.source.NaverApiClient

class AppContainer {
    private var naverApiClient: NaverApiClient? = null

    fun provideApiClient(): NaverApiClient {
        return naverApiClient ?: NaverApiClient.create(BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET).apply {
            naverApiClient = this
        }
    }
}