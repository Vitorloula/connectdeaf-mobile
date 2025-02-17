package com.connectdeaf.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectdeaf.domain.model.ScheduleItem
import com.connectdeaf.domain.model.Service
import com.connectdeaf.network.retrofit.ApiServiceFactory
import kotlinx.coroutines.launch
import java.util.UUID

class ScheduleViewModel : ViewModel() {

    private val _appointments = mutableStateOf<List<Appointment>>(emptyList())
    val appointments: State<List<Appointment>> = _appointments

    private val _scheduleItems = mutableStateOf<List<ScheduleItem>>(emptyList())
    val scheduleItems: State<List<ScheduleItem>> = _scheduleItems

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _selectedStatus = mutableStateOf<String?>(null)
    val selectedStatus: State<String?> = _selectedStatus

    private val _showFilterDialog = mutableStateOf(false)
    val showFilterDialog: State<Boolean> = _showFilterDialog

    fun fetchCustomerAppointments(userId: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context = context)
                val apiAppointmentService = apiServiceFactory.appointmentService
                val response = apiAppointmentService.getAppointmentsByUser(userId)

                _appointments.value = response

                val scheduleList = response.map { appointment ->
                    appointment.id?.let {
                        ScheduleItem(
                            appointmentId = it,
                            serviceId = appointment.service.id,
                            serviceName = appointment.service.name,
                            clientName = appointment.customer.name,
                            status = translateStatus(appointment.status),
                            professionalName = appointment.professional.name,
                            date = appointment.schedule?.date ?: "Data não definida",
                            statusColor = getStatusColor(appointment.status),
                            startTime = appointment.schedule?.startTime ?: "Horário não definido",
                            address = appointment.customer.addresses.firstOrNull()?.street ?: "Endereço não informado"
                        )
                    }
                }


                _scheduleItems.value = scheduleList as List<ScheduleItem>

            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "Erro ao buscar agendamentos: ${e.message}")
            }
        }
    }

    fun fetchProfessionalAppointments(professionalId: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context = context)
                val apiAppointmentService = apiServiceFactory.appointmentService
                val response = apiAppointmentService.getAppointmentsByProfessional(professionalId)

                _appointments.value = response

                Log.d("ScheduleViewModel", "Agendamentos: $response")
                Log.d("ScheduleViewModel", "Agendamentos: ${response.size}")

                val scheduleList = response.map { appointment ->
                    appointment.id?.let {
                        ScheduleItem(
                            appointmentId = it,
                            serviceId = appointment.service.id,
                            serviceName = appointment.service.name,
                            clientName = appointment.customer.name,
                            status = translateStatus(appointment.status),
                            professionalName = appointment.professional.name,
                            date = appointment.schedule?.date ?: "Data não definida",
                            statusColor = getStatusColor(appointment.status),
                            startTime = appointment.schedule?.startTime ?: "Horário não definido",
                            address = appointment.customer.addresses.firstOrNull()?.street ?: "Endereço não informado"
                        )
                    }
                }

                _scheduleItems.value = scheduleList as List<ScheduleItem>

            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "Erro ao buscar agendamentos: ${e.message}")
            }
        }
    }

    fun deleteAppointment (appointmentId: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context = context)
                val apiAppointmentService = apiServiceFactory.appointmentService
                apiAppointmentService.deleteAppointment(appointmentId)
            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "Erro ao deletar agendamento: ${e.message}")
            }
        }

    }


    private suspend fun fetchServiceDetails(serviceId: UUID, context: Context): Service {
        val apiServiceFactory = ApiServiceFactory(context)
        val serviceService = apiServiceFactory.serviceService
        return serviceService.getService(serviceId.toString())
    }

    fun filterSchedules() {

        val filteredAppointments = _appointments.value.filter { appointment ->

            val searchQueryLower = _searchQuery.value.lowercase()
            val matchesSearchQuery = appointment.customer.name.lowercase().contains(searchQueryLower) ||
                    appointment.professional.name.lowercase().contains(searchQueryLower) ||
                    appointment.service.name.lowercase().contains(searchQueryLower)

            val matchesStatus = _selectedStatus.value?.let { status ->
                appointment.status == status
            } ?: true

            matchesSearchQuery && matchesStatus
        }

        _scheduleItems.value = filteredAppointments.map { appointment ->
            ScheduleItem(
                appointmentId = appointment.id!!,
                serviceId = appointment.service.id,
                serviceName = appointment.service.name,
                clientName = appointment.customer.name,
                status = translateStatus(appointment.status),
                professionalName = appointment.professional.name,
                date = appointment.schedule?.date ?: "Data não definida",
                statusColor = getStatusColor(appointment.status),
                startTime = appointment.schedule?.startTime ?: "Horário não definido",
                address = appointment.customer.addresses.firstOrNull()?.street ?: "Endereço não informado"
            )
        }
    }

    fun acceptAppointment(appointmentId: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context = context)
                val apiAppointmentService = apiServiceFactory.appointmentService
                apiAppointmentService.acceptAppointment(appointmentId)
                Toast.makeText(context, "Agendamento aceito com sucesso!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "Erro ao aceitar agendamento: ${e.message}")
                Toast.makeText(context, "Erro ao aceitar agendamento", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun rejectAppointment(appointmentId: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context = context)
                val apiAppointmentService = apiServiceFactory.appointmentService
                apiAppointmentService.rejectAppointment(appointmentId)
                Toast.makeText(context, "Agendamento recusado com sucesso!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "Erro ao recusar agendamento: ${e.message}")
                Toast.makeText(context, "Erro ao recusar agendamento", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun translateStatus(status: String): String {
        return when (status) {
            "APPROVED" -> "Aprovado"
            "REJECTED" -> "Rejeitado"
            "PENDING" -> "Pendente"
            else -> "Desconhecido"
        }
    }

    private fun getStatusColor(status: String): Color {
        if (status == "APPROVED") {
            return Color.Green
        } else if (status == "REJECTED") {
            return Color.Red
        } else {
            return Color.Gray
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterSchedules()
    }
}


