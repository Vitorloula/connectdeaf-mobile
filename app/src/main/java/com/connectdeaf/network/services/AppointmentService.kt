package com.connectdeaf.network.services

import com.connectdeaf.network.dtos.AppointmentRequest
import com.connectdeaf.viewmodel.Appointment
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppointmentService {
    @POST("api/appointments")
    suspend fun postAppointment(
        @Body appointmentRequest: AppointmentRequest
    )

    @GET("api/appointments/professional/{professionalId}")
    suspend fun getAppointmentsByProfessionalId(@Path("professionalId") userId: String): List<Appointment>

    @DELETE("api/appointments/{appointmentId}")
    suspend fun deleteAppointment(
        @Path("appointmentId") appointmentId: String
    )

    @GET("api/appointments/customer/{userId}")
        suspend fun getAppointmentsByUser(
            @Path("userId") userId: String
        ): List<Appointment>


    @GET("api/appointments/professional/{professionalId}")
    suspend fun getAppointmentsByProfessional(
        @Path("professionalId") professionalId: String
    ): List<Appointment>


        @PATCH("api/appointments/{appointmentId}/approve")
        suspend fun acceptAppointment(
            @Path("appointmentId") appointmentId: String
        )

        @PATCH("api/appointments/{appointmentId}/reject")
        suspend fun rejectAppointment(
            @Path("appointmentId") appointmentId: String
        )

}
