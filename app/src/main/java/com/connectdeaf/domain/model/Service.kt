package com.connectdeaf.domain.model

data class Service(
    val id: String, // Alterado para String, pois o JSON usa UUID
    val name: String,
    val description: String,
    val value: Double,
    val professional: ProfessionalService // Inclui o objeto profissional
)

data class ProfessionalService(
    val id: String, // UUID do profissional
    val name: String,
    val email: String,
    val phoneNumber: String,
    val qualification: String,
    val areaOfExpertise: String,
    val workStartTime: String, // Horário de início do trabalho
    val workEndTime: String, // Horário de término do trabalho
    val breakDuration: String, // Duração do intervalo no formato ISO 8601
    val addresses: List<AddressService> // Lista de endereços
)

data class AddressService(
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String
)