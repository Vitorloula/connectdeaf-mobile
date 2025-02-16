package com.connectdeaf.ui.components.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.connectdeaf.domain.model.chat.Message
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CardMensagem(mensagem: Message, enviadoPorMim: Boolean) {
    Column(
        horizontalAlignment = if (enviadoPorMim) Alignment.End else Alignment.Start,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        // Card da mensagem
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (enviadoPorMim) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = mensagem.corpo ?: "Mensagem inválida",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (enviadoPorMim) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Data/Hora da mensagem
        Text(
            text = formatarDataHora(mensagem.dataHora),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// Função para formatar a data/hora
private fun formatarDataHora(dataHora: Timestamp?): String {
    if (dataHora == null) return "Data desconhecida"

    // Converte o Timestamp para Date
    val date: Date = dataHora.toDate()

    // Formata a data/hora
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(date)
}