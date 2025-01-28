package com.connectdeaf.network.dtos

data class ProfessionalRequest(
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val areaOfExpertise: String,
    val qualification: String,
    val addresses: List<AddressRequest>
) {
    override fun toString(): String {
        return "ProfessionalRequest(name='$name', email='$email', password='*****', phoneNumber='$phoneNumber', areaOfExpertise='$areaOfExpertise', qualification='$qualification', addresses=$addresses)"
    }
}
