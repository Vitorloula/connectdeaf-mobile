import com.connectdeaf.domain.model.Professional
import com.connectdeaf.domain.model.User
import java.time.Duration
import java.time.LocalTime

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val isEmailValid: Boolean = true,
    val isPhoneValid: Boolean = true,
    val isUserCreated: Boolean = false,
    val errorMessage: String? = null,

    val isProfessionalFlow: Boolean = false,
    val areaOfExpertise: String = "",
    val qualification: String ="",
    val qualifications: List<String> = emptyList(),
    val workStartTime: LocalTime = LocalTime.now(),
    val workEndTime: LocalTime = LocalTime.now(),
    val breakDuration: String = "",

    val cep: String = "",
    val state: String = "",
    val city: String = "",
    val street: String = "",
    val neighborhood: String = "",
    val number: String = "",
    val complement: String = "",
    val isCepValid: Boolean = true,

    val user: User? = null,
    val professional: Professional? = null
) {
    val isUserValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() &&
                password.isNotBlank()

    val isProfessionalValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() &&
                password.isNotBlank() && areaOfExpertise.isNotBlank() && qualification.isNotBlank() &&
                workStartTime.isBefore(workEndTime) && breakDuration.isNotBlank()

    val isAddressValid: Boolean
        get() = cep.isNotBlank() && state.isNotBlank() && city.isNotBlank() &&
                street.isNotBlank() && neighborhood.isNotBlank() && number.isNotBlank() && isCepValid

}
