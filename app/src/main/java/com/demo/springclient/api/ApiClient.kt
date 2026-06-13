package com.demo.springclient.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    /**
     * for real device (adb reverse tcp:8080 tcp:8080)
     */
    private const val BASE_URL = "http://localhost:8080/"

    /**
     * for Gm
     */
//    private const val BASE_URL = "http://10.0.3.2:8080/"
//    private const val BASE_URL = "http://10.0.2.2:8080/"

    val service: ApiService by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}