package com.connectdeaf.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectdeaf.network.dtos.AssessmentRequest
import com.connectdeaf.network.retrofit.ApiServiceFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssessmentViewModel(private val context: Context) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _professionalId = MutableStateFlow<String?>(null)
    val professionalId: StateFlow<String?> = _professionalId

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun fetchProfessionalId(serviceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apiServiceFactory = ApiServiceFactory(context)
                val apiService = apiServiceFactory.serviceService
                val service = apiService.getService(serviceId)
                _professionalId.value = service.professional.id
            } catch (e: Exception) {
                _message.value = "Erro ao buscar profissional: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createAssessment(
        text: String,
        rating: Int,
        userId: String,
        professionalId: String,
        serviceId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apiServiceFactory = ApiServiceFactory(context)
                val assessmentService = apiServiceFactory.assessmentService
                val assessmentRequest = AssessmentRequest(
                    text = text,
                    rating = rating,
                    userId = userId,
                    professionalId = professionalId,
                    serviceId = serviceId
                )
                assessmentService.createAssessment(assessmentRequest)
                _message.value = "Avaliação criada com sucesso!"
            } catch (e: Exception) {
                _message.value = "Erro ao criar avaliação: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
