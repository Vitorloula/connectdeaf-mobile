package com.connectdeaf.viewmodel.uistate

import android.net.Uri
import com.connectdeaf.domain.model.Service

data class ServicesProfessionalUiState(
    val nameService: String = "",
    val description: String = "",
    val price: String = "",
    val state: String = "",
    val city: String = "",
    val categories: String = "",
    val imageUri: Uri? = null,
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val nameServiceError: String? = null,
    val descriptionError: String? = null,
    val priceError: String? = null,
    val stateError: String? = null,
    val cityError: String? = null,
    val categoriesError: String? = null
) {
    val isFormValid: Boolean
        get() = nameService.isNotBlank() &&
                description.isNotBlank() &&
                price.toDoubleOrNull()?.let { it > 0 } == true &&
                state.isNotBlank() &&
                city.isNotBlank() &&
                categories.isNotBlank()
}
