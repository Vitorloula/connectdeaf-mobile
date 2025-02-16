package com.connectdeaf.domain.model.chat

import com.google.firebase.Timestamp


data class Message(
    val corpo: String? = null,
    val usuario: FirebaseUser? = null,
    val dataHora: Timestamp? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "corpo" to corpo,
            "usuario" to usuario?.toMap(),
            "dataHora" to dataHora
        )
    }
}
