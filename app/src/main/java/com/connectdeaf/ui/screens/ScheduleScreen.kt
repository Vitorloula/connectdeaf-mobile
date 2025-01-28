package com.connectdeaf.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.connectdeaf.ui.components.DrawerMenu
import com.connectdeaf.ui.components.ScheduleCard
import com.connectdeaf.viewmodel.DrawerViewModel
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreen(
    navController: NavController,
    drawerViewModel: DrawerViewModel = viewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val schedules = listOf(
        ScheduleItem("Design para loja de flores", "Olivia da Silva", "Cancelado", Color.Red),
        ScheduleItem("Logotipo", "João Carlos", "Em espera", Color.Blue),
        ScheduleItem("Protótipo", "Carla Ilane", "Concluído", Color.Green)
    )

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val filteredSchedules = schedules.filter {
        (searchQuery.isBlank() || it.serviceName.contains(searchQuery, ignoreCase = true) || it.clientName.contains(searchQuery, ignoreCase = true)) &&
                (selectedStatus == null || it.status == selectedStatus)
    }

    DrawerMenu(
        navController = navController,
        scope = scope,
        drawerViewModel = drawerViewModel,
    ) {
        Scaffold(
            topBar = {
                com.connectdeaf.ui.components.TopAppBar(
                    onOpenDrawerMenu = { scope.launch { drawerViewModel.openMenuDrawer() } },
                    onOpenDrawerNotifications = { scope.launch { drawerViewModel.openNotificationsDrawer() } },
                    showBackButton = true,
                    navController = navController
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { showFilterDialog = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Filter Icon",
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { newValue -> searchQuery = newValue },
                        placeholder = {
                            Text(
                                "Pesquisar por serviço, prestador...",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search Icon")
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de agendamentos filtrados
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredSchedules) { schedule ->
                        ScheduleCard(schedule)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Mensagem caso nenhum item corresponda ao filtro
                if (filteredSchedules.isEmpty()) {
                    Text(
                        text = "Nenhum agendamento encontrado",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Dialog de filtro
            if (showFilterDialog) {
                AlertDialog(
                    onDismissRequest = { showFilterDialog = false },
                    title = {
                        Text(
                            text = "Filtrar por: ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    text = {
                        Column {
                            listOf("Concluído", "Cancelado", "Em espera").forEach { status ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedStatus = if (selectedStatus == status) null else status
                                            showFilterDialog = false
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedStatus == status,
                                        onClick = {
                                            selectedStatus = if (selectedStatus == status) null else status
                                            showFilterDialog = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = status)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showFilterDialog = false }) {
                            Text(text = "Fechar")
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ScheduleScreenPreview() {
    ScheduleScreen(navController = rememberNavController())
}



data class ScheduleItem(
    val serviceName: String,
    val clientName: String,
    val status: String,
    val statusColor: Color
)
