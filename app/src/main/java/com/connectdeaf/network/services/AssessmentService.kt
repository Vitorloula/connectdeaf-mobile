package com.connectdeaf.network.services

import com.connectdeaf.domain.model.Assessment
import com.connectdeaf.network.dtos.AssessmentRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AssessmentService {
    @GET("api/comment/service/{service_id}")
    suspend fun getAssessmentByService(@Path("service_id") id: String): List<Assessment>

    @POST("api/comment")
    suspend fun createAssessment(@Body assessment: AssessmentRequest): Assessment
}