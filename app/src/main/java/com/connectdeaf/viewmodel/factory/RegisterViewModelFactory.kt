package com.connectdeaf.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.connectdeaf.data.repository.UserRepository
import com.connectdeaf.viewmodel.RegisterViewModel

class RegisterViewModelFactory() : ViewModelProvider.Factory {
    private val userRepository: UserRepository = UserRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
