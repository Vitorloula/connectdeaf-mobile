package com.connectdeaf.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectdeaf.domain.model.Address
import com.connectdeaf.domain.model.Assessment
import com.connectdeaf.domain.model.Service
import com.connectdeaf.domain.model.User
import com.connectdeaf.network.dtos.UploadDocumentRequest
import com.connectdeaf.network.dtos.VerifyDocumentRequest
import com.connectdeaf.network.retrofit.ApiServiceFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
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
    val user: User? // Para clientes
)

// Estado para guardar o resultado do upload/verificação de documento
data class DocumentState(
    val uploadedPath: String? = null,
    val verificationResult: String? = null,
    val isVerified: Boolean = false
)

class ProfileViewModel : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    // Armazena info sobre o certificado (upload/verif)
    private val _documentState = MutableStateFlow(DocumentState())
    val documentState: StateFlow<DocumentState> = _documentState

    fun fetchProfile(userId: String, role: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context)
                if (role == "ROLE_PROFESSIONAL") {
                    val professional = apiServiceFactory.professionalService.getProfessional(userId)
                    _profile.value = Profile(
                        id = professional.id,
                        name = professional.name,
                        addresses = professional.addresses,
                        description = professional.description,
                        imageUrl = professional.imageUrl,
                        category = professional.category,
                        services = null,
                        assessments = professional.assessments,
                        user = null
                    )
                    fetchServicesByProfessional(userId, context)
                } else {
                    val user = apiServiceFactory.userService.getUserById(userId)
                    _profile.value = Profile(
                        id = user.id.toString(),
                        name = user.name,
                        addresses = user.addresses,
                        description = null,
                        imageUrl = null,
                        category = null,
                        services = null,
                        assessments = null,
                        user = user
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching profile", e)
            }
        }
    }

    fun fetchServicesByProfessional(professionalId: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context)
                val services =
                    apiServiceFactory.serviceService.getServicesByProfessional(professionalId)
                _profile.value = _profile.value?.copy(services = services)
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching services", e)
            }
        }
    }

    fun fetchAppointments(userId: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context)
                val appointments =
                    apiServiceFactory.appointmentService.getAppointmentsByProfessionalId(userId)
                _appointments.value = appointments
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching appointments", e)
            }
        }
    }

    /**
     * Converte arquivo (Uri) em Base64, sem quebras de linha.
     */
    private fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) Base64.encodeToString(bytes, Base64.NO_WRAP) else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Faz upload do arquivo e depois verifica se é válido, usando o nome do profissional do _profile.
     */
    fun uploadAndVerifyCertificate(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val base64Content = uriToBase64(context, uri)
                if (base64Content.isNullOrEmpty()) {
                    Log.e("ProfileViewModel", "Falha ao converter arquivo para Base64.")
                    return@launch
                }

                val filename = "certificado_${System.currentTimeMillis()}.png"
                val apiFactory = ApiServiceFactory(context)

                // 1) UPLOAD
                val uploadResponse = apiFactory.documentApiService.uploadFile(
                    UploadDocumentRequest(
                        filename = filename,
                        content = base64Content
                    )
                )
                val uploadedPath = uploadResponse.response // ex: "documents/file.pdf"

                // 2) VERIFICAÇÃO (usa o nome do profissional do perfil)
                val professionalName = _profile.value?.name ?: ""
                val verifyResponse = apiFactory.documentApiService.verifyFile(
                    VerifyDocumentRequest(
                        document_path = uploadedPath,
                        professional_name = professionalName
                    )
                )
                val result = verifyResponse.response // ex: "Válido." ou "Inválido"

                // Se a API SEMPRE retorna "Válido." (com ponto), ajustamos a comparação:
                val isValid = result.contains("Válido")

                // Atualiza estado
                _documentState.value = _documentState.value.copy(
                    uploadedPath = uploadedPath,
                    verificationResult = result,
                    isVerified = isValid
                )

                Log.d("ProfileViewModel", "Verificação concluída: $isValid")

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ProfileViewModel", "Erro ao fazer upload/verificação: ${e.message}")
            }
        }
    }
}

/**
 * Função auxiliar para calcular receita mensal (opcional)
 */
data class MonthlyRevenue(
    val month: String,
    val revenue: Double
)

fun calculateMonthlyRevenue(appointments: List<Appointment>): List<MonthlyRevenue> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val revenueByMonth = appointments.groupBy { appointment ->
        val date = LocalDate.parse(appointment.schedule?.date, formatter)
        "${date.year}-${date.monthValue.toString().padStart(2, '0')}"
    }.map { (month, appointmentsInMonth) ->
        MonthlyRevenue(
            month = month,
            revenue = appointmentsInMonth.sumOf { it.service.value }
        )
    }
    return revenueByMonth.sortedBy { it.month }
}
