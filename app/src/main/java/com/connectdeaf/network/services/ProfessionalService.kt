package com.connectdeaf.network.services


import com.connectdeaf.network.dtos.TimeSlotRequest
import com.connectdeaf.domain.model.Professional
import com.connectdeaf.network.dtos.ProfessionalRequest
import com.connectdeaf.viewmodel.Profile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfessionalService {
    @GET("api/professionals/{id}")
    suspend fun getProfessional(@Path("id") id: String): Profile

    @GET("api/professionals/{professionalId}/{date}")
    suspend fun fetchTimeSlots(
        @Path("professionalId") professionalId: String,
        @Path("date") date: String
    ): Response<List<TimeSlotRequest>>


    @POST("api/professionals")
    suspend fun createProfessional(@Body professional: ProfessionalRequest): Professional

}