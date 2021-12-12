package com.bruce.room.repository.net

import com.bruce.room.repository.net.interceptor.HttpCommonInterceptor
import com.bruce.room.repository.net.interceptor.HttpLogger
import com.bruce.room.repository.net.interceptor.TokenErrorInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import com.bruce.room.BuildConfig


object NetworkManagerK {

    val retrofitService: RetrofitService
    private val loggingInterceptor = HttpLoggingInterceptor(HttpLogger())

    init {
        Timber.i("NetworkManagerK")

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        // 创建 OKHttpClient
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(HttpCommonInterceptor())
            .addInterceptor(TokenErrorInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                )
            )
            .baseUrl(BuildConfig.BASE_SERVER_URL)
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)
    }
}