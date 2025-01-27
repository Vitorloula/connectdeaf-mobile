package com.connectdeaf.network.services

import com.connectdeaf.domain.model.Assessment
import retrofit2.http.GET
import retrofit2.http.Path

interface AssessmentService {
    @GET("api/comment/service/{service_id}")
    suspend fun getAssessmentByService(@Path("service_id") id: String): List<Assessment>
}