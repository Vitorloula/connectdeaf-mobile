package com.connectdeaf.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingIntercep
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "https://connectdeaf-dev.azurewebsites.net/"

    fun api(getToken: (() -> String?)? = null): ApiService {
        val clientBuilder = OkHttpClient.Builder()

        if (getToken != null) {
            clientBuilder.addInterceptor(AuthInterceptor(getToken))
        }

        clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        return retrofit.create(ApiService::class.java)
    }
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

        return retrofit.create(ApiService::class.java)
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
