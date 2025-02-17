package com.connectdeaf.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectdeaf.domain.model.Address
import com.connectdeaf.domain.model.Assessment
import com.connectdeaf.domain.model.Service
import com.connectdeaf.domain.model.User
import com.connectdeaf.network.retrofit.ApiServiceFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Modelo de perfil genérico
data class Profile(
    val id: String,
    val name: String,
    val addresses: List<Address>,
    val description: String?,
    val imageUrl: String?,
    val category: List<String>?, // Apenas para profissionais
    val services: List<Service>?, // Apenas para profissionais
    val assessments: List<Assessment>?,
    val user: User? // O user agora faz parte do Profile de forma imutável
)

// ViewModel para gerenciar perfis
class ProfileViewModel : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    fun fetchProfile(userId: String, role: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context)

                if (role == "ROLE_PROFESSIONAL") {
                    // Busca o perfil do profissional e os serviços dele
                    val professional = apiServiceFactory.professionalService.getProfessional(userId)

                    // Atualiza o Profile com os dados do profissional
                    _profile.value = Profile(
                        id = professional.id,
                        name = professional.name,
                        addresses = professional.addresses,
                        description = professional.description,
                        imageUrl = professional.imageUrl,
                        category = professional.category,
                        services = null,
                        assessments = professional.assessments,
                        user = null // Profissionais não têm o campo user
                    )

                    fetchServicesByProfessional(userId, context)

                    Log.d("ProfileViewModel", "Professional Profile Loaded: ${_profile.value}")
                } else {
                    // Busca o perfil do cliente (usuário comum)
                    val client = apiServiceFactory.userService.getUserById(userId)

                    // Atualiza o Profile com os dados do cliente
                    _profile.value = Profile(
                        id = client.id.toString(),
                        name = client.name,
                        addresses = client.addresses,
                        description = null,
                        imageUrl = null,
                        category = null, // Clientes não têm categorias
                        services = null, // Clientes não têm serviços
                        assessments = null, // Ajuste conforme sua API
                        user = client // Salva o cliente como parte do perfil
                    )

                    Log.d("ProfileViewModel", "Client Profile Loaded: ${_profile.value}")
                }

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching profile", e)
            }
        }
    }

    fun fetchServicesByProfessional(professionalId: String, context: Context){
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context)
                val services = apiServiceFactory.serviceService.getServicesByProfessional(professionalId)
                _profile.value = _profile.value?.copy(services = services)
            }
            catch (e: Exception){
                Log.e("ProfileViewModel", "Error fetching services", e)
            }
        }
    }

    fun fetchAppointments(userId: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context)
                val appointments = apiServiceFactory.appointmentService.getAppointmentsByProfessionalId(userId)

                _appointments.value = appointments

                Log.d("ProfileViewModel", "Appointments Loaded: ${appointments.size}")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching appointments", e)
            }
        }
    }

}

data class MonthlyRevenue(
    val month: String, // Por exemplo "2025-02"
    val revenue: Double
)

fun calculateMonthlyRevenue(appointments: List<Appointment>): List<MonthlyRevenue> {
    // Supondo que a data vem no formato "yyyy-MM-dd"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Agrupa os appointments por ano-mês e soma os valores
    val revenueByMonth = appointments.groupBy { appointment ->
        // Converte a data para LocalDate e extrai o ano-mês
        val date = LocalDate.parse(appointment.schedule.date, formatter)
        date.year.toString() + "-" + date.monthValue.toString().padStart(2, '0')
    }.map { (month, appointmentsInMonth) ->
        MonthlyRevenue(
            month = month,
            revenue = appointmentsInMonth.sumOf { it.service.value }
        )
    }

    // Ordena os resultados cronologicamente
    return revenueByMonth.sortedBy { it.month }
}

