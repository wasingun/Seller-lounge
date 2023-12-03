package com.wasingun.seller_lounge.di

import com.google.firebase.auth.FirebaseAuth
import com.wasingun.seller_lounge.data.datasource.AuthDataSource
import com.wasingun.seller_lounge.data.datasource.AuthFirebaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthDataSourceModule {

    @Provides
    fun provideFirebaseAuth(firebaseAuth: FirebaseAuth): AuthDataSource {
        return AuthFirebaseDataSource(firebaseAuth)
    }
}