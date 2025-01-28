package com.connectdeaf.network.dtos

data class TimeSlotRequest(val startTime: String, val endTime: String)
data class AppointmentRequest(val date: String, val time: String)