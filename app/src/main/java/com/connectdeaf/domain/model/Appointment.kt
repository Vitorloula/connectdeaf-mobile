package com.connectdeaf.viewmodel


import com.connectdeaf.domain.model.Service
import java.util.UUID

data class Schedule(
    val id: UUID,
    val date: String,
    val startTime: String,
    val endTime: String,
    val professionalId: UUID
)

data class Appointment(
    val id: UUID? = null,             // ID único
    val customerId: UUID,             // ID do cliente associado
    val professionalId: UUID,         // ID do profissional associado
    val service: Service,              // ID do serviço associado
    val schedule: Schedule,     // ID do horário (opcional)
    val status:   String                // Status do agendamento (ex.: "Pendente", "Aprovada")
)
