package com.connectdeaf.domain.model


import com.connectdeaf.viewmodel.Appointment
import java.util.UUID


data class Professional(
    val id: UUID? = null,                     // ID único gerado automaticamente
    val name: String,                        // Nome do usuário
    val email: String,                       // E-mail único do usuário
    val password: String,                    // Senha do usuário
    val phoneNumber: String,         // Número de telefone opcional
    val areaOfExpertise: String = "",
    val qualification: String ="",
    val addresses: List<Address> = emptyList(), // Lista de endereços (inicializa vazia)
    val appointments: List<Appointment> = emptyList(), // Lista de compromissos (inicializa vazia)
)
