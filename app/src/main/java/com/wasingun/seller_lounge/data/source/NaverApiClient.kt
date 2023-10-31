package com.wasingun.seller_lounge.data.source

import com.google.gson.Gson
import com.wasingun.seller_lounge.data.model.productsearch.SearchResponse
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NaverApiClient {
    @POST("datalab/shopping/category/keywords")
    suspend fun getComparedKeywordData(
        @Body request: KeywordRequest
    ): KeywordResponse

    @GET("search/shop.json")
    suspend fun getSearchResult(@Query("query") query: String) : SearchResponse

    companion object {
        private const val BASE_URL = "https://openapi.naver.com/v1/"
        private val gson = Gson()

        fun create(clientId: String, clientSecret: String): NaverApiClient {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val header = Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("X-Naver-Client-Id", clientId)
                    .addHeader("X-Naver-Client-Secret", clientSecret)
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(header)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(NaverApiClient::class.java)
        }
    }
}