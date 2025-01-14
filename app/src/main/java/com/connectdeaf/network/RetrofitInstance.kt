package com.connectdeaf.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

}
