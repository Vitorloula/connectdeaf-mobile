package com.connectdeaf.network.dtos

import java.math.BigDecimal

data class PaymentRequest(
    val amount: BigDecimal,
    val email: String
)

data class PaymentResponse(
    val paymentId: Long,
    val status: String,
    val qrCode: String,
    val qrCodeBase64: String,
    val paymentLink: String
)