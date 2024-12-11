package com.connectdeaf.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val isEmailValid: Boolean = true,
    val isPhoneValid: Boolean = true
) {
    private val isInputValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && password.isNotBlank()

    val isFormValid: Boolean
        get() = isInputValid && isEmailValid && isPhoneValid
}


class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onEmailChange(newEmail: String) {
        val isValid = isValidEmail(newEmail)
        _uiState.update { it.copy(email = newEmail, isEmailValid = isValid) }
    }

    fun onPhoneChange(newPhone: String) {
        val isValid = isValidPhone(newPhone)
        _uiState.update { it.copy(phone = newPhone, isPhoneValid = isValid) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        val phoneRegex = "^[0-9]{10,11}$".toRegex()
        return phone.matches(phoneRegex)
    }
}
