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

    fun fetchProfile(userId: String, role: String, context: Context) {
        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context)

                if (role == "professional") {
                    // Busca o perfil do profissional e os serviços dele
                    val professional = apiServiceFactory.professionalService.getProfessional(userId)
                    val services = apiServiceFactory.serviceService.getServicesByProfessional(userId)

                    // Atualiza o Profile com os dados do profissional
                    _profile.value = Profile(
                        id = professional.id,
                        name = professional.name,
                        addresses = professional.addresses,
                        description = professional.description,
                        imageUrl = professional.imageUrl,
                        category = professional.category,
                        services = services,
                        assessments = professional.assessments,
                        user = null // Profissionais não têm o campo user
                    )

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
}
