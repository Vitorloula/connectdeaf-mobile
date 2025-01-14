package com.connectdeaf.viewmodel


import java.util.UUID


data class Appointment(
    val id: UUID? = null,             // ID único
    val customerId: UUID,             // ID do cliente associado
    val professionalId: UUID,         // ID do profissional associado
    val serviceId: UUID,              // ID do serviço associado
    val scheduleId: UUID? = null,     // ID do horário (opcional)
    val status: String                // Status do agendamento (ex.: "Pendente", "Aprovada")
)
