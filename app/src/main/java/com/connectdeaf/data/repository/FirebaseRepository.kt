package com.connectdeaf.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Registro de usuário
    suspend fun registerUser(email: String, password: String, name: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid

            if (uid != null) {
                val user = hashMapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "created_at" to System.currentTimeMillis()
                )
                firestore.collection("users").document(uid).set(user).await()
            }
            true
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro no cadastro: ${e.message}")
            false
        }
    }

    // Login com email e senha
    suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro no login: ${e.message}")
            false
        }
    }

    suspend fun resetPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao enviar email de recuperação: ${e.message}")
            false
        }
    }

    suspend fun getUserName(): String? {
        return try {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                val snapshot = firestore.collection("users").document(uid).get().await()
                snapshot.getString("name") // Retorna o nome salvo no Firestore
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao buscar nome do usuário: ${e.message}")
            null
        }
    }

    suspend fun getUserEmail(): String? {
        return try {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                val snapshot = firestore.collection("users").document(uid).get().await()
                snapshot.getString("email") // Retorna o nome salvo no Firestore
                } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao buscar nome do usuário: ${e.message}")
            null
        }
    }

    suspend fun getUserId(): String? {
        return try {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                val snapshot = firestore.collection("users").document(uid).get().await()
                snapshot.getString("uid") // Retorna o nome salvo no Firestore
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao buscar nome do usuário: ${e.message}")
            null
        }
    }

    // Logout
    fun logout() {
        auth.signOut()
    }

    // Verifica se o usuário está logado
    fun isUserLogged(): Boolean {
        return auth.currentUser != null
    }
}


