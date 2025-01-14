package com.connectdeaf.network

import com.connectdeaf.domain.model.User
import com.connectdeaf.network.dtos.UserRequest
import com.connectdeaf.viewmodel.Assessment
import com.connectdeaf.viewmodel.Profile
import com.connectdeaf.viewmodel.Service
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
    @GET("api/professionals/{id}")
    suspend fun getProfessional(@Path("id") id: String): Profile


    @GET("api/professionals/{id}/services")
    suspend fun getServices(@Path("id") id: String): List<Service>


    @GET("api/professionals/{id}/assessments")
    suspend fun getAssessments(@Path("id") id: String): List<Assessment>


    @POST("api/users")
    suspend fun createUser(@Body user: UserRequest): User
}
