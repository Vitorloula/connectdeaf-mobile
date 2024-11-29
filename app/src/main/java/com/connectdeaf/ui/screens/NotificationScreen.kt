package com.connectdeaf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.connectdeaf.R
import com.connectdeaf.ui.components.NotificationCard
import com.connectdeaf.data.model.Notification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavHostController
) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if (
                                navController.previousBackStackEntry != null
                            ) {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.29f))
                        Image(
                            painter = painterResource(id = R.drawable.logo_horizontal),
                            contentDescription = "Logo Horizontal",
                            modifier = Modifier.size(166.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0XFF478FCC)
                )
            )
        },
        content = { paddingValues ->
            // Conteúdo da tela, ajustado pelo padding do Scaffold
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF478FCC))
                    .padding(paddingValues), // Ajuste para não sobrepor a topBar
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationCard(
                            notification = notification,
                            onClick = {
                                // Ação ao clicar na notificação
                            }
                        )
                    }
                }
            }
        }
    )
}
