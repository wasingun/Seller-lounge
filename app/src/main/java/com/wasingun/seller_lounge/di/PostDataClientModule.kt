package com.wasingun.seller_lounge.di

import com.google.gson.Gson
import com.wasingun.seller_lounge.network.ApiCallAdapterFactory
import com.wasingun.seller_lounge.network.PostDataClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostDataClientModule {

    private const val BASE_URL =
        "https://seller-lounge-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val gson = Gson()

    @Singleton
    @Provides
    @Named("postDataOkHttpClient")
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Singleton
    @Provides
    @Named("postDataRetrofit")
    fun provideRetrofit(@Named("postDataOkHttpClient") okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ApiCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiClient(@Named("postDataRetrofit") retrofit: Retrofit): PostDataClient {
        return retrofit.create(PostDataClient::class.java)
    }
}