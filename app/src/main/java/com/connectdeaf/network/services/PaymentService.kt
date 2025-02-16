package com.connectdeaf.network.services

import com.connectdeaf.network.dtos.PaymentRequest
import com.connectdeaf.network.dtos.PaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentService {
    @POST("/api/payments/pix")
    suspend fun createPixPayment(
        @Body request: PaymentRequest
    ): Response<PaymentResponse>
}