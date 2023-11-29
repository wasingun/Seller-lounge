package com.wasingun.seller_lounge.network

import com.wasingun.seller_lounge.data.model.productsearch.SearchResponse
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NaverApiClient {
    @POST("datalab/shopping/category/keywords")
    suspend fun getComparedKeywordData(
        @Body request: KeywordRequest
    ): ApiResponse<KeywordResponse>

    @GET("search/shop.json")
    suspend fun getSearchResult(
        @Query("query") query: String,
        @Query("start") start: Int,
        @Query("sort") sort: String,
    ): SearchResponse
}