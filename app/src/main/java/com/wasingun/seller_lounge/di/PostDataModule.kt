package com.wasingun.seller_lounge.di

import com.google.gson.Gson
import com.wasingun.seller_lounge.network.ApiCallAdapterFactory
import com.wasingun.seller_lounge.network.PostDataApiClient
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
object PostDataModule {

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
    fun provideRetrofit(@Named("postDataOkHttpClient")okHttp: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(PostDataModule.BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(PostDataModule.gson))
            .addCallAdapterFactory(ApiCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiClient(@Named("postDataRetrofit") retrofit: Retrofit): PostDataApiClient {
        return retrofit.create(PostDataApiClient::class.java)
    }
}