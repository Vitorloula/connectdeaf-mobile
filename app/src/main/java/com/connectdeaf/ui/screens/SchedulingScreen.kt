package com.connectdeaf.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.connectdeaf.ui.components.DatePickerDialog
import com.connectdeaf.ui.components.DrawerMenu
import com.connectdeaf.ui.components.ProcessStepsIndicator
import com.connectdeaf.viewmodel.DrawerViewModel
import com.connectdeaf.viewmodel.SchedulingViewModel
import kotlinx.coroutines.launch


@Composable
fun SchedulingScreen(
    navController: NavController,
    drawerViewModel: DrawerViewModel = viewModel(),
    SchedulingViewModel: SchedulingViewModel = viewModel()
) {
    // Para lidar com eventos de navegação
    val context = LocalContext.current

    // Para lidar com escopos de corrotina
    val scope = rememberCoroutineScope()

    // Variáveis de estado para a data e horário selecionados
    var selectedDate by remember { mutableStateOf("Selecione uma data") }
    var selectedTime by remember { mutableStateOf("Selecione um horário") }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val currentStep = 1

    // Opções de horário disponíveis
    val availableTimes = listOf("08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00")

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
                    .padding(32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Agendar Serviços",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Selecione uma data e horário disponível para o serviço",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(25.dp))

                // Seletor de data
                Text(
                    text = "Selecione um dia",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Botão para abrir o DatePicker
                Button(
                    onClick = { showDatePickerDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedDate)
                }

                // Dialog de data
                if (showDatePickerDialog) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePickerDialog = false },
                        onDateSelected = { year, month, dayOfMonth ->
                            selectedDate = "$dayOfMonth/${month + 1}/$year"
                            showDatePickerDialog = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Seletor de horário
                Text(
                    text = "Selecione um horário",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Grid de horários disponíveis
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 colunas por linha
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(availableTimes) { time ->
                        // Verifique se o item está selecionado
                        val isSelected = selectedTime == time

                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .clickable {
                                    selectedTime = if (isSelected) "" else time
                                },
                            shape = MaterialTheme.shapes.medium,

                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White
                            )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)

                            ) {
                                Text(
                                    text = time,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Exibe a data e horário selecionado
                Text(
                    text = "Data selecionada: $selectedDate",
                    fontSize = 14.sp,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Horário selecionado: $selectedTime",
                    fontSize = 14.sp,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(200.dp))

                Button(
                    onClick = { navController.navigate("") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedDate != "Selecione uma data" && selectedTime != "Selecione um horário") MaterialTheme.colorScheme.primary
                        else Color(0xFF999999),
                    )
                ) {
                    Text("Continuar")
                }
            }
        }
    }
}

@Preview
@Composable
fun SchedulingScreenPreview() {
    SchedulingScreen(navController = rememberNavController())
}

