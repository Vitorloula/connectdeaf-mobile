package com.connectdeaf.domain.model


import java.util.UUID


data class Address(
    val id: UUID? = null,       // ID único gerado
    val cep: String,            // CEP do endereço
    val street: String,         // Rua
    val number: String,         // Número
    val complement: String? = null, // Complemento opcional
    val neighborhood: String,   // Bairro
    val city: String,           // Cidade
    val state: String,          // Estado
    val userId: UUID? = null    // ID do usuário associado
)
