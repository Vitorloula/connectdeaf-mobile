package com.connectdeaf.network.services

import com.connectdeaf.domain.model.User
import com.connectdeaf.network.dtos.UserRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @POST("api/users")
    suspend fun createUser(@Body user: UserRequest): User

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: String): User
}