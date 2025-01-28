package com.connectdeaf.network.dtos

import java.time.Duration
import java.time.LocalTime

data class ProfessionalRequest(
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val areaOfExpertise: String,
    val qualification: String,
    val workStartTime: String,
    val workEndTime: String,
    val breakDuration: String,
    val addresses: List<AddressRequest>
) {
    override fun toString(): String {
        return "ProfessionalRequest(name='$name', email='$email', password='*****', phoneNumber='$phoneNumber', areaOfExpertise='$areaOfExpertise', qualification='$qualification', workStartTime=$workStartTime, workEndTime=$workEndTime, addresses=$addresses)"
    }
}
