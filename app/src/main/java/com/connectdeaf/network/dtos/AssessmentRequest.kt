package com.connectdeaf.network.dtos

data class AssessmentRequest(
    val text: String,
    val rating: Int,
    val userId: String,
    val professionalId: String,
    val serviceId: String
)