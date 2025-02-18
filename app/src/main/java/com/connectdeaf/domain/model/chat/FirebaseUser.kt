package com.connectdeaf.domain.model.chat

data class FirebaseUser(
    val uid: String? = null,
    val email: String? = null,
    val name: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "email" to email,
            "name" to name
        )
    }
}