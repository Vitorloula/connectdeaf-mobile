package com.connectdeaf.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectdeaf.domain.model.Assessment
import com.connectdeaf.domain.model.Service
import com.connectdeaf.network.retrofit.ApiServiceFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServiceViewModel : ViewModel() {

    private val _serviceState = MutableStateFlow<Service?>(null)
    val serviceState: StateFlow<Service?> = _serviceState

    private val _serviceAssessment = MutableStateFlow<List<Assessment>?>(null)
    val serviceAssessment: StateFlow<List<Assessment>?> = _serviceAssessment

    fun fetchServiceDetail(serviceId: String, context: Context) {

        viewModelScope.launch {
            val apiServiceFactory = ApiServiceFactory(context)

            val serviceService = apiServiceFactory.serviceService

            val serviceDetail = serviceService.getService(serviceId)

            _serviceState.emit(serviceDetail)
        }
    }

    fun fetchServiceAssessment(serviceId: String, context: Context){
        viewModelScope.launch {
            val apiServiceFactory = ApiServiceFactory(context)
            val assessmentService = apiServiceFactory.assessmentService
            val serviceAssessment = assessmentService.getAssessmentByService(serviceId)
            _serviceAssessment.emit(serviceAssessment)
        }
    }
}
