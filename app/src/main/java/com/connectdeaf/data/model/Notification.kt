package com.connectdeaf.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

data class Notification(
    val title: String,
    val message: String,
    val time: String,
    val icon: ImageVector = Icons.Default.Notifications
)

val notifications = listOf(
    Notification(
        title = "Nova mensagem",
        message = "Você recebeu uma nova mensagem de João.",
        time = "10:45 AM",
        icon = Icons.Default.Notifications
    ),
    Notification(
        title = "Atualização disponível",
        message = "Uma nova versão do aplicativo está pronta para download.",
        time = "9:30 AM",
        icon = Icons.Default.Notifications
    ),
    Notification(
        title = "Evento hoje",
        message = "Não se esqueça do evento marcado para hoje às 14h.",
        time = "Ontem",
        icon = Icons.Default.Notifications
    )
)