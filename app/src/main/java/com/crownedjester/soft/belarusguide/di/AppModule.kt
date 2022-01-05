package com.crownedjester.soft.belarusguide.di

import com.crownedjester.soft.belarusguide.common.Constants
import com.crownedjester.soft.belarusguide.data.KrokappApi
import com.crownedjester.soft.belarusguide.domain.repository.RemoteServicesRepository
import com.crownedjester.soft.belarusguide.domain.repository.RemoteServicesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesRemoteServices(retrofit: Retrofit): KrokappApi =
        retrofit.create(KrokappApi::class.java)

    @Provides
    @Singleton
    fun providesRemoteRepository(remoteApi: KrokappApi): RemoteServicesRepository =
        RemoteServicesRepositoryImpl(remoteApi)
}

fun createClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BASIC

    return OkHttpClient().newBuilder()
        .addInterceptor(interceptor)
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}