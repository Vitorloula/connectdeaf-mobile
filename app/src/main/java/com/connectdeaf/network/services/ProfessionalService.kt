package com.connectdeaf.network.services

import com.connectdeaf.network.dtos.TimeSlotRequest
import com.connectdeaf.viewmodel.Profile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProfessionalService {
    @GET("api/professionals/{id}")
    suspend fun getProfessional(@Path("id") id: String): Profile

    @GET("api/professionals/{professionalId}/{date}")
    fun fetchTimeSlots(
        @Path("professionalId") professionalId: String,
        @Path("date") date: String
    ): List<TimeSlotRequest>

}