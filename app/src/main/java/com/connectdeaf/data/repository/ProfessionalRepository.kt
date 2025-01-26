package com.connectdeaf.data.repository

import android.content.Context
import android.util.Log
import com.connectdeaf.domain.model.Professional
import com.connectdeaf.network.dtos.ProfessionalRequest
import com.connectdeaf.network.retrofit.ApiServiceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfessionalRepository {
    // Função que cria o usuário
    suspend fun createProfessional(professionalRequest: ProfessionalRequest, context: Context): Result<Professional> {

        val apiServiceFactory = ApiServiceFactory(context)
        val professionalService = apiServiceFactory.professionalService

        return try {
            val createdProfessional = withContext(Dispatchers.IO) {
                professionalService.createProfessional(professionalRequest)
            }

            if (createdProfessional.id != null) {
                Result.success(createdProfessional)
            } else {
                Result.failure(Exception("Falha ao criar profissional!"))
            }
        } catch (e: Exception) {
            Log.e("ProfessionalRepository", "Erro ao criar profissional: ${e.message}")
            Result.failure(e)
        }
    }
}
