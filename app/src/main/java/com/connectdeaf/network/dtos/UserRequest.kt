package com.connectdeaf.network.dtos

data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val addresses: List<AddressRequest>
) {
    override fun toString(): String {
        return "UserRequest(name='$name', email='$email', password='*****', phoneNumber='$phoneNumber', addresses=$addresses)"
    }
}
