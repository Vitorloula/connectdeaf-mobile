package com.connectdeaf.domain.model.chat

data class Conversa(
    var id: String? = null,
    val mensagens: List<Message>? = null,
    val usuario1: FirebaseUser? = null,
    val usuario2: FirebaseUser? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "mensagens" to mensagens?.map { it.toMap() },
            "usuario1" to usuario1?.toMap(),
            "usuario2" to usuario2?.toMap()
        )
    }
}