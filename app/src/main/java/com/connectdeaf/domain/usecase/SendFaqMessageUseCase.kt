package com.connectdeaf.domain.usecase

import com.connectdeaf.domain.model.FaqRequest
import com.connectdeaf.domain.model.FaqResponse
import com.connectdeaf.data.repository.FaqRepository

class SendFaqMessageUseCase(private val faqRepository: FaqRepository) {
    suspend operator fun invoke(message: String): FaqResponse {
        val request = FaqRequest(user_message = message)
        return faqRepository.sendMessage(request)
    }
}
