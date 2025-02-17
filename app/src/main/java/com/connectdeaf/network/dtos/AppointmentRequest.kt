package com.connectdeaf.network.dtos

data class TimeSlotRequest(val startTime: String, val endTime: String)
data class AppointmentRequest(val customerId: String, val professionalId: String, val serviceId: String, val date: String, val startTime: String, val endTime: String)