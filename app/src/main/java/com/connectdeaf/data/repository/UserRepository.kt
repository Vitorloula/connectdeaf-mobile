package com.connectdeaf.data.repository

import android.util.Log
import com.connectdeaf.domain.model.User
import com.connectdeaf.network.RetrofitInstance
import com.connectdeaf.network.dtos.UserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    // Função que cria o usuário
    suspend fun createUser(userRequest: UserRequest): Result<User> {
        return try {
            val createdUser = withContext(Dispatchers.IO) {
                RetrofitInstance.api.createUser(userRequest)
            }

            if (createdUser.id != null) {
                Result.success(createdUser)
            } else {
                Result.failure(Exception("Falha ao criar usuário!"))
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Erro ao criar usuário: ${e.message}")
            Result.failure(e)
        }
    }
}
