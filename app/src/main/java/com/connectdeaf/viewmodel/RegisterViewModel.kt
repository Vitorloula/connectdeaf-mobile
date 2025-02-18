package com.connectdeaf.viewmodel

import RegisterUiState
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectdeaf.data.repository.FirebaseRepository
import com.connectdeaf.data.repository.ProfessionalRepository
import com.connectdeaf.data.repository.UserRepository
import com.connectdeaf.domain.model.Professional
import com.connectdeaf.domain.model.User
import com.connectdeaf.network.dtos.AddressRequest
import com.connectdeaf.network.dtos.ProfessionalRequest
import com.connectdeaf.network.dtos.UserRequest
import com.connectdeaf.utils.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val professionalRepository: ProfessionalRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    var registerResult: ((Boolean) -> Unit)? = null

    // Estados de usuário e endereço
    private val _uiState = MutableStateFlow(RegisterUiState(
        qualifications = listOf(
            "Ensino Médio",
            "Graduação",
            "Pós-Graduação",
            "Mestrado",
            "Doutorado"
        )
    ))
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Atualiza os dados do usuário
    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, isEmailValid = Validator.isValidEmail(newEmail)) }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone, isPhoneValid = Validator.isValidPhone(newPhone)) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun updateFlow(isProfessional: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isProfessionalFlow = isProfessional)
        }
    }

    fun onAreaOfExpertiseChange(newAreaOfExpertise: String) {
        _uiState.update { it.copy(areaOfExpertise = newAreaOfExpertise) }
    }

    fun onQualificationChange(newQualification: String) {
        _uiState.update { it.copy(qualification = newQualification) }
    }

    fun onWorkStartTimeChange(newWorkStartTime: LocalTime) {
        _uiState.update { it.copy(workStartTime = newWorkStartTime) }
    }

    fun onWorkEndTimeChange(newWorkEndTime: LocalTime) {
        _uiState.update { it.copy(workEndTime = newWorkEndTime) }
    }

    fun onBreakDurationChange(newBreakDuration: String) {
        _uiState.update { it.copy(breakDuration = newBreakDuration) }
    }

    // Atualiza os dados do endereço
    fun onCepChange(newCep: String) {
        val filteredCep = newCep.filter { it.isDigit() }.take(8)
        _uiState.update {
            it.copy(
                cep = filteredCep,
                isCepValid = filteredCep.matches(Regex("\\d{8}"))
            )
        }
    }

    fun onStateChange(newState: String) {
        _uiState.update { it.copy(state = newState.trim()) }
    }

    fun onCityChange(newCity: String) {
        _uiState.update { it.copy(city = newCity.trim()) }
    }

    fun onStreetChange(newStreet: String) {
        _uiState.update { it.copy(street = newStreet.trim()) }
    }

    fun onNeighborhoodChange(newNeighborhood: String) {
        _uiState.update { it.copy(neighborhood = newNeighborhood.trim()) }
    }

    fun onNumberChange(newNumber: String) {
        _uiState.update { it.copy(number = newNumber.trim()) }
    }

    fun onComplementChange(newComplement: String) {
        _uiState.update { it.copy(complement = newComplement.trim()) }
    }


    // Resetar campos
    fun resetFields() {
        _uiState.update { RegisterUiState() }
    }

    // Registra o profissional, incluindo o endereço
    fun registerProfessional(context: Context) {
        val state = _uiState.value

        if (!state.isUserValid) {
            Log.e("RegisterViewModel", "Formulário inválido. Verifique os campos obrigatórios.")
            return
        }

        val professional = Professional(
            name = state.name,
            email = state.email,
            password = state.password,
            phoneNumber = state.phone,
            areaOfExpertise = state.areaOfExpertise,
            qualification = state.qualification,
            workStartTime = state.workStartTime.toString(),
            workEndTime = state.workEndTime.toString(),
            breakDuration = Duration.ofMinutes(state.breakDuration.toLong()).toString()
        )

        val addresses = listOf(
            AddressRequest(
                cep = state.cep,
                state = state.state,
                city = state.city,
                street = state.street,
                neighborhood = state.neighborhood,
                number = state.number,
                complement = state.complement
            )
        )

        val professionalRequest = ProfessionalRequest(
            name = professional.name,
            email = professional.email,
            password = professional.password,
            phoneNumber = professional.phoneNumber,
            areaOfExpertise = professional.areaOfExpertise,
            qualification = professional.qualification,
            workStartTime = professional.workStartTime,
            workEndTime = professional.workEndTime,
            breakDuration = professional.breakDuration,
            addresses = addresses
        )

        Log.d("RegisterViewModel", "Dados do ProfessionalRequest: $professionalRequest")

        // Enviar para a API
        firebaseRegister(state.email, state.password, state.name) { success ->
            if (success) {
                registerResult?.invoke(true)
            } else {
                registerResult?.invoke(false)
                Log.e("RegisterViewModel", "Erro no cadastro com Firebase")
            }

        }
        sendProfessionalRequest(professionalRequest, context)
    }

    // Método para enviar os dados para a API
    private fun sendProfessionalRequest(professionalRequest: ProfessionalRequest, context: Context) {
        viewModelScope.launch {
            try {

                // Chama o repositório para criar o usuário
                val result = professionalRepository.createProfessional(professionalRequest, context)

                // Verifica o resultado da criação
                result.onSuccess { createdProfessional ->
                    _uiState.update { it.copy(isUserCreated = true, professional = createdProfessional) }
                    Log.d("RegisterViewModel", "Profissional criado com sucesso: $createdProfessional")
                }.onFailure { error ->
                    _uiState.update { it.copy(isUserCreated = false, errorMessage = error.message) }
                    Log.e("RegisterViewModel", "Erro ao criar profissional: ${error.message}")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isUserCreated = false, errorMessage = e.message) }
                Log.e("RegisterViewModel", "Erro ao criar profissional: ${e.message}")
            }
        }
    }

    // Registra o usuário, incluindo o endereço
    fun registerUser(context: Context) {
        val state = _uiState.value

        // Validar todos os campos
        if (!state.isUserValid) {
            Log.e("RegisterViewModel", "Formulário inválido. Verifique os campos obrigatórios.")
            return
        }

        // Criar o usuário
        val user = User(
            name = state.name,
            email = state.email,
            password = state.password,
            phoneNumber = state.phone
        )

        // Criar o endereço
        val addresses = listOf(
            AddressRequest(
                cep = state.cep,
                state = state.state,
                city = state.city,
                street = state.street,
                neighborhood = state.neighborhood,
                number = state.number,         // Adicionado
                complement = state.complement  // Adicionado
            )
        )


        // Criar a solicitação final para enviar à API
        val userRequest = UserRequest(
            name = user.name,
            email = user.email,
            password = user.password,
            phoneNumber = user.phoneNumber,
            addresses = addresses
        )

        Log.d("RegisterViewModel", "Dados do UserRequest: $userRequest")

        // Enviar para a API
        firebaseRegister(state.email, state.password, state.name) { success ->
            if (success) {
                registerResult?.invoke(true)
            } else {
                registerResult?.invoke(false)
                Log.e("RegisterViewModel", "Erro no cadastro com Firebase")
            }

        }
        sendUserRequest(userRequest, context)
    }

    // Método para enviar os dados para a API
    private fun sendUserRequest(userRequest: UserRequest, context: Context) {
        viewModelScope.launch {
            try {

                // Chama o repositório para criar o usuário
                val result = userRepository.createUser(userRequest, context)

                // Verifica o resultado da criação
                result.onSuccess { createdUser ->
                    _uiState.update { it.copy(isUserCreated = true, user = createdUser) }
                    Log.d("RegisterViewModel", "Usuário criado com sucesso: $createdUser")
                }.onFailure { error ->
                    _uiState.update { it.copy(isUserCreated = false, errorMessage = error.message) }
                    Log.e("RegisterViewModel", "Erro ao criar usuário: ${error.message}")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isUserCreated = false, errorMessage = e.message) }
                Log.e("RegisterViewModel", "Erro ao criar usuário: ${e.message}")
            }
        }
    }

    fun firebaseRegister(email: String, password: String, name: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = firebaseRepository.registerUser(email, password, name)
            onResult(success)
        }
    }
}
