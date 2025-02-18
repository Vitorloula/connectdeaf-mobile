package com.connectdeaf.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectdeaf.data.repository.ServicesRepository
import com.connectdeaf.domain.model.Service
import com.connectdeaf.network.dtos.ServiceRequest
import com.connectdeaf.viewmodel.uistate.ServicesProfessionalUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiceProfessionalViewModel(
    private val servicesRepository: ServicesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServicesProfessionalUiState())
    val uiState: StateFlow<ServicesProfessionalUiState> = _uiState.asStateFlow()

    private fun onServicesChange(services: List<Service>) {
        _uiState.update {
            it.copy(
                services = services,
                isLoading = false
            )
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun getServicesByProfessional(professionalId: String, context: Context) {
        setLoadingState(true)
        viewModelScope.launch {
            try {
                val result = servicesRepository.getServicesByProfessional(professionalId, context)
                result
                    .onSuccess { services -> onServicesChange(services) }
                    .onFailure {
                        Log.e("ServiceProfessionalViewModel", "Erro ao buscar serviços: ${it.message}")
                        setLoadingState(false)
                    }
            } catch (e: Exception) {
                Log.e("ServiceProfessionalViewModel", "Erro ao buscar serviços: ${e.message}")
                setLoadingState(false)
            }
        }
    }

    fun createServiceByProfessional(
        professionalId: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val state = _uiState.value

        // Validação dos campos obrigatórios
        if (state.nameService.isBlank()) {
            onError("O nome do serviço é obrigatório.")
            return
        }
        if (state.description.isBlank()) {
            onError("A descrição do serviço é obrigatória.")
            return
        }
        if (state.price.isBlank()) {
            onError("O preço do serviço é obrigatório.")
            return
        }
        if (state.state.isBlank()) {
            onError("O estado é obrigatório.")
            return
        }
        if (state.city.isBlank()) {
            onError("A cidade é obrigatória.")
            return
        }

        // Converte o preço para Double, garantindo que valores inválidos sejam tratados
        val priceDouble = state.price.toDoubleOrNull() ?: -1.0
        if (priceDouble <= 0) {
            onError("Preço inválido. Digite um valor maior que 0.")
            return
        }

        val serviceRequest = ServiceRequest(
            name = state.nameService,
            description = state.description,
            value = priceDouble,
            state = state.state,
            city = state.city
        )

        Log.d("ServiceProfessionalViewModel", "ServiceRequest: $serviceRequest")

        setLoadingState(true)
        viewModelScope.launch {
            try {
                val result = servicesRepository.createService(professionalId, serviceRequest, context)
                result
                    .onSuccess { service ->
                        onServicesChange(listOf(service))
                        onSuccess()
                    }
                    .onFailure {
                        Log.e("ServiceProfessionalViewModel", "Erro ao criar serviço: ${it.message}")
                        onError("Erro ao criar serviço. Tente novamente.")
                        setLoadingState(false)
                    }
            } catch (e: Exception) {
                Log.e("ServiceProfessionalViewModel", "Erro ao criar serviço: ${e.message}", e)
                onError("Erro ao criar serviço. Tente novamente.")
                setLoadingState(false)
            }
        }
    }

    fun deleteService(serviceId: String, context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        setLoadingState(true)
        viewModelScope.launch {
            try {
                val result = servicesRepository.deleteService(serviceId, context)
                result
                    .onSuccess {
                        val updatedServices = _uiState.value.services.filterNot { it.id == serviceId }
                        onServicesChange(updatedServices)
                        onSuccess()
                    }
                    .onFailure {
                        Log.e("ServiceProfessionalViewModel", "Erro ao deletar serviço: ${it.message}")
                        onError("Erro ao deletar serviço. Tente novamente.")
                        setLoadingState(false)
                    }
            } catch (e: Exception) {
                Log.e("ServiceProfessionalViewModel", "Erro ao deletar serviço: ${e.message}", e)
                onError("Erro ao deletar serviço. Tente novamente.")
                setLoadingState(false)
            }
        }
    }

    fun onNameServiceChange(newNameService: String) {
        _uiState.update { it.copy(nameService = newNameService) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onPriceChange(newPrice: String) {
        _uiState.update { it.copy(price = newPrice) }
    }

    fun onCategoryChange(newCategories: String) {
        _uiState.update { it.copy(categories = newCategories) }
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun onStateChange(newState: String) {
        _uiState.update { it.copy(state = newState) }
    }

    fun onCityChange(newCity: String) {
        _uiState.update { it.copy(city = newCity) }
    }
}