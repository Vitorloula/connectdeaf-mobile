package com.connectdeaf.domain.model

data class FaqRequest(
    val user_message: String
)

data class FaqResponse(
    val response: String
)
