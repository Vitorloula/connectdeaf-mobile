
package com.connectdeaf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Service(
    val id: String,
    val name: String,
    val description: String,
    val category: List<String>,
    val value: String,
    val imageUrl: String
)

data class Assessment(
    val name: String,
    val stars: Int,
    val description: String
)

data class Profile(
    val id: String,
    val name: String,
    val city: String,
    val state: String,
    val description: String,
    val imageUrl: String,
    val category: List<String>,
    val services: List<Service>,
    val assessments: List<Assessment>
)

class ProfileViewModel : ViewModel() {
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    fun fetchProfile(professionalId: String) {
        viewModelScope.launch {
            // Simule o fetch de dados (substitua com chamada de API real)
            val mockProfile = Profile(
                id = professionalId,
                name = "Fernando Byano",
                city = "Quixadá",
                state = "CE",
                description = "Lorem ipsum dolor sit amet...",
                imageUrl = "",
                category = listOf("IA", "Designer"),
                services = listOf(
                    Service(
                        id = "1",
                        name = "Criar branding",
                        description = "Ajuda no desenvolvimento da identidade visual.",
                        category = listOf("Design", "Branding"),
                        value = "R$170",
                        imageUrl = ""
                    )
                ),
                assessments = listOf(
                    Assessment(
                        name = "Luis Estevam",
                        stars = 4,
                        description = "Ótimo profissional!"
                    )
                )
            )
            _profile.value = mockProfile
        }
    }
}
