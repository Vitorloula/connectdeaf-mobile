import com.connectdeaf.domain.model.User

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val isEmailValid: Boolean = true,
    val isPhoneValid: Boolean = true,
    val isUserCreated: Boolean = false,
    val errorMessage: String? = null,
    val cep: String = "",
    val state: String = "",
    val city: String = "",
    val street: String = "",
    val neighborhood: String = "",
    val number: String = "",         // Adicionado
    val complement: String = "",    // Adicionado
    val isCepValid: Boolean = true,
    val user: User? = null
) {
    val isUserValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() &&
                password.isNotBlank()

    val isFormValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() &&
                password.isNotBlank() && cep.isNotBlank() && state.isNotBlank() &&
                city.isNotBlank() && street.isNotBlank() && neighborhood.isNotBlank() &&
                number.isNotBlank() && isCepValid
}
