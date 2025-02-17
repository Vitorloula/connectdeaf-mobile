package com.connectdeaf.domain.model

import androidx.compose.ui.graphics.Color
import java.util.UUID


data class ScheduleItem(
    val appointmentId: UUID,
    val serviceId: String,
    val serviceName: String,
    val clientName: String,
    val professionalName: String,
    val date: String,
    val status: String,
    val statusColor: Color,
    val startTime: String,
    val address: String
)