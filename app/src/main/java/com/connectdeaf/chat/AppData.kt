package com.connectdeaf.chat

import android.util.Log
import com.connectdeaf.domain.model.chat.Conversa
import com.connectdeaf.domain.model.User


class AppData private constructor() {
    private var conversas: MutableList<Conversa> = mutableListOf()
    private var usuarioLogado: User? = null

    companion object {
        @Volatile private var instance: AppData? = null

        // Implementando o método getInstance com double-checked locking para melhorar performance
        fun getInstance(): AppData =
            instance ?: synchronized(this) {
                instance ?: AppData().also { instance = it }
            }
    }

    fun getConversaByUsers(usuarioId1: String?, usuarioId2: String?): Conversa? {
        if (usuarioId1 == null || usuarioId2 == null) {
            Log.e("AppData", "IDs de usuário inválidos")
            return null
        }

        return conversas.find { conversa ->
            (conversa.usuario1?.uid == usuarioId1 && conversa.usuario2?.uid == usuarioId2) ||
                    (conversa.usuario1?.uid == usuarioId2 && conversa.usuario2?.uid == usuarioId1)
        }
    }

    fun getConversaById(id: String): Conversa? {
        return conversas.find { conversa -> conversa.id == id }
    }

    // Agora, a lista é imutável
    fun setConversas(conversas: List<Conversa>) {
        this.conversas = conversas.toMutableList()  // Copia para garantir que não alterem diretamente
    }

    fun addConversa(conversa: Conversa) {
        conversas.add(conversa)
    }

    fun getUsuarioLogado(): User? {
        return usuarioLogado
    }

    fun setUsuarioLogado(usuario: User?) {
        this.usuarioLogado = usuario  // Permite que o usuário logado seja alterado para null, se necessário
    }
}
