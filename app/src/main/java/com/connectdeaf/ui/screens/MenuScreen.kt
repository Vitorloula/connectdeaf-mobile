package com.connectdeaf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.connectdeaf.ui.components.MenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            // Colocando a TopAppBar dentro do Scaffold
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
                // Exemplo de itens de menu
                MenuItem(icon = Icons.Default.Home, text = "Home", onClick = { /* Ação */ })
                MenuItem(icon = Icons.Default.DateRange, text = "Agendamentos", onClick = { /* Ação */ })
                MenuItem(icon = Icons.Default.Phone, text = "Serviços", onClick = { /* Ação */ })
                MenuItem(icon = Icons.Default.Person, text = "Perfil", onClick = { /* Ação */ })
            }
        }
    )
}
