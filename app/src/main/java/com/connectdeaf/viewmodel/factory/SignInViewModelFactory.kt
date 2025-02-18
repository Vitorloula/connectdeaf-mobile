package com.connectdeaf.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.connectdeaf.chat.DataRepository
import com.connectdeaf.data.repository.AuthRepository
import com.connectdeaf.data.repository.FirebaseRepository
import com.connectdeaf.viewmodel.SignInViewModel

class SignInViewModelFactory(
    private val authRepository: AuthRepository,
    private val firebaseRepository: FirebaseRepository,
    private val dataRepository: DataRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignInViewModel(authRepository, firebaseRepository, dataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
