package com.connectdeaf.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddressUiState(
    val cep: String = "",
    val state: String = "",
    val city: String = "",
    val street: String = "",
    val neighborhood: String = ""
) {
    val isInputValid: Boolean
        get() = cep.isNotBlank() && state.isNotBlank() && city.isNotBlank() && street.isNotBlank() && neighborhood.isNotBlank()
}

class AddressViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    fun onCepChange(newCep: String) {
        _uiState.update { it.copy(cep = newCep) }
    }

    fun onStateChange(newState: String) {
        _uiState.update { it.copy(state = newState) }
    }

    fun onCityChange(newCity: String) {
        _uiState.update { it.copy(city = newCity) }
    }

    fun onStreetChange(newStreet: String) {
        _uiState.update { it.copy(street = newStreet) }
    }

    fun onNeighborhoodChange(newNeighborhood: String) {
        _uiState.update { it.copy(neighborhood = newNeighborhood) }
    }
}