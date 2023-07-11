package com.addenisov00.courseproject.common.di

import com.addenisov00.courseproject.common.Constants
import com.addenisov00.courseproject.data.network.MessageApiClient
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiClient(client: OkHttpClient): MessageApiClient = run {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(
                @OptIn(ExperimentalSerializationApi::class)
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(MessageApiClient::class.java)
    }

    @Provides
    fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", Constants.authorization)
                        .build()
                )
            }.build()
}