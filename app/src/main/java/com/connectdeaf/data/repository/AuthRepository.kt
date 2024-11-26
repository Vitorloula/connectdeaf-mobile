package com.connectdeaf.data.repository

class AuthRepository {
    // Simulação de autenticação para fins de exemplo
    suspend fun login(email: String, password: String): Boolean {
        // Adicione a lógica de autenticação aqui (API ou banco de dados)
        return email == "teste@exemplo.com" && password == "123456"
    }
}
