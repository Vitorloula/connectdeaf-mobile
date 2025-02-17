package com.connectdeaf.network.services

import com.connectdeaf.network.dtos.AppointmentRequest
import com.connectdeaf.viewmodel.Appointment
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppointmentService {
    @POST("api/appointments")
    suspend fun postAppointment(
        @Query("customerId") customerId: String,
        @Query("professionalId") professionalId: String,
        @Query("serviceId") serviceId: String,
        @Body appointmentRequest: AppointmentRequest
    )

    @GET("api/appointments/professional/{professionalId}")
    suspend fun getAppointmentsByProfessionalId(@Path("professionalId") userId: String): List<Appointment>

}
