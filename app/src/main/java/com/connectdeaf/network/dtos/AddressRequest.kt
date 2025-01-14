package com.connectdeaf.network.dtos

data class AddressRequest(
    val cep: String,
    val street: String,
    val number: String,         // Adicionado
    val complement: String,     // Adicionado
    val neighborhood: String,
    val city: String,
    val state: String
)
 {
    override fun toString(): String {
        return "AddressRequest(cep='$cep', state='$state', city='$city', street='$street', number='$number', complement='$complement',neighborhood='$neighborhood')"
    }
}
