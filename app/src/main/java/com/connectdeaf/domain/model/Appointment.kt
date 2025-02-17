package com.connectdeaf.viewmodel


import java.util.UUID


data class Appointment(
    val id: UUID? = null,
    val customer: Customer,
    val professional: Professional,
    val service: Service,
    val schedule: Schedule?,
    val status: String
)

data class Customer(
    val id: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val addresses: List<UserAdress>
)

data class Professional(
    val id: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val qualification: String,
    val areaOfExpertise: String,
    val workStartTime: String,
    val workEndTime: String,
    val breakDuration: String,
    val addresses: List<UserAdress>
)

data class Service(
    val id: UUID,
    val name: String,
    val description: String,
    val value: Double
)

data class Schedule(
    val id: UUID,
    val professionalId: UUID,
    val date: String,
    val startTime: String,
    val endTime: String
)

data class UserAdress(
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String
)