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


data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val accessToken: String, val expiresIn: Int)


interface ApiService {
    @GET("api/professionals/{id}")
    suspend fun getProfessional(@Path("id") id: String): Profile

    @GET("api/services/{id}")
    suspend fun getServices(@Path("id") id: String): List<Service>

    @POST("api/users")
    suspend fun createUser(@Body user: UserRequest): User

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
