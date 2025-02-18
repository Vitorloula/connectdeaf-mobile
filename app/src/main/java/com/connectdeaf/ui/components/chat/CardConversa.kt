package com.connectdeaf.ui.components.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.connectdeaf.R
import com.connectdeaf.chat.DataRepository
import com.connectdeaf.domain.model.chat.Conversa
import com.connectdeaf.domain.model.chat.Message

@Composable
fun CardConversa(conversa: Conversa, onClick: (Conversa) -> Unit = {}) {
    val context = LocalContext.current
    val dataRepository = DataRepository(context)
    var userName by remember { mutableStateOf<String?>(null) }
    val userName2 = if (conversa.usuario1?.name == userName) {
        conversa.usuario2?.name ?: "Unknown User"
    } else {
        conversa.usuario1?.name ?: "Unknown User"
    }

    LaunchedEffect(Unit) {
        userName = dataRepository.getCurrentUserName()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onClick(conversa) }
            .fillMaxWidth()
    ) {
        // Avatar do usuário
        AsyncImage(
            model = R.drawable.avatar, // Substitua pelo URL ou recurso correto
            contentDescription = "Avatar do usuário",
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .clip(CircleShape)
        )

        // Detalhes da conversa
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            // Nome do usuário
            Text(
                text = userName2,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Última mensagem
            Text(
                text = getLastMessage(conversa.mensagens),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Função para obter a última mensagem
private fun getLastMessage(mensagens: List<Message>?): String {
    return mensagens?.lastOrNull()?.corpo ?: "Nenhuma mensagem"
}