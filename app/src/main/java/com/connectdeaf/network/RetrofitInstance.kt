package com.connectdeaf.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL_MAIN = "https://connectdeaf-dev.azurewebsites.net/"
    private const val BASE_URL_AI = "https://webapp-connectdeaf-dev.azurewebsites.net/"

    private val customClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val mainApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_MAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .client(customClient)
            .build()
            .create(ApiService::class.java)
    }

    val aiApi: FAQApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_AI)
            .addConverterFactory(GsonConverterFactory.create())
            .client(customClient)
            .build()
            .create(FAQApiService::class.java)
    }
}
