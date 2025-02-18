package com.connectdeaf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectdeaf.domain.usecase.SendFaqMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Representa uma mensagem de chat, seja do Bot ou do usuário.
 */
data class ChatMessage(
    val sender: String,
    val text: String,
    val isBot: Boolean,
    val isLoading: Boolean = false
)

class FAQViewModel(
    private val sendFaqMessageUseCase: SendFaqMessageUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "FAQViewModel"
    }

    // Lista de mensagens no chat
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    // Indica se estamos carregando (chamada de API)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        Log.d(TAG, "init: Iniciando FAQViewModel com mensagem de boas-vindas do bot.")
        // Mensagem inicial do bot
        _messages.value = listOf(
            ChatMessage(
                sender = "CDBot",
                text = "Oi! 👋 Eu sou o **CDBot**, seu bot de dúvidas aqui no app. 🤖\n" +
                        "Estou aqui para responder suas perguntas frequentes e te ajudar " +
                        "com tudo o que precisar. É só perguntar, e eu dou aquela mãozinha! ✌️✨",
                isBot = true
            )
        )
    }

    fun sendMessage(userMessage: String) {
        Log.d(TAG, "sendMessage: Recebeu mensagem do usuário: $userMessage")

        // Adiciona a mensagem do usuário e uma mensagem de bot "carregando"
        _messages.value += ChatMessage(
            sender = "Você",
            text = userMessage,
            isBot = false
        )
        _messages.value += ChatMessage(
            sender = "CDBot",
            text = "Processando sua pergunta...",
            isBot = true,
            isLoading = true
        )

        // Lança a corrotina para obter a resposta
        viewModelScope.launch {
            try {
                Log.d(TAG, "Chamando sendFaqMessageUseCase com: \"$userMessage\"")
                val result = sendFaqMessageUseCase(userMessage)
                Log.d(TAG, "Resposta recebida do servidor: \"${result.response}\"")

                // Remove a mensagem "carregando" e adiciona a resposta real
                _messages.value = _messages.value.dropLast(1) + ChatMessage(
                    sender = "CDBot",
                    text = result.response,
                    isBot = true
                )
            } catch (e: Exception) {
                Log.e(TAG, "Exceção ao enviar mensagem FAQ: ${e.localizedMessage}", e)

                // Remove a mensagem "carregando" e adiciona a mensagem de erro
                _messages.value = _messages.value.dropLast(1) + ChatMessage(
                    sender = "CDBot",
                    text = "Erro ao se comunicar com o servidor FAQ.",
                    isBot = true
                )
            }
        }
    }

}
