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
import androidx.compose.runtime.LaunchedEffect
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
import com.connectdeaf.viewmodel.DrawerViewModel
import com.connectdeaf.viewmodel.AppointmentViewModel
import kotlinx.coroutines.launch


@Composable
fun AppointmentScreen(
    navController: NavController,
    drawerViewModel: DrawerViewModel = viewModel(),
    appointmentViewModel: AppointmentViewModel = viewModel(),
    ServiceId: String,  // Mudado para String (ou mantenha Int conforme necessidade)
    ProfessionalId: String  // Mudado para String (ou mantenha Int conforme necessidade)
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val selectedDate = appointmentViewModel.selectedDate.value
    val selectedTime = appointmentViewModel.selectedTime.value
    val showDatePickerDialog = remember { mutableStateOf(false) }

    val availableTimes = appointmentViewModel.availableTimeSlots

    LaunchedEffect(selectedDate) {
        if (selectedDate != "Selecione uma data") {
            appointmentViewModel.fetchTimeSlots(ProfessionalId, selectedDate, context)
        }
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

                // Seção de Seleção de Data
                Text(
                    text = "Selecione um dia",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { showDatePickerDialog.value = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedDate)
                }

                if (showDatePickerDialog.value) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePickerDialog.value = false },
                        onDateSelected = { year, month, dayOfMonth ->
                            val date = "$dayOfMonth/${month + 1}/$year"
                            appointmentViewModel.selectDate(date)
                            showDatePickerDialog.value = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Seção de Seleção de Horário
                Text(
                    text = "Selecione um horário",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Acessando a lista real de horários a partir do MutableState
                    items(availableTimes.value) { timeSlot ->
                        val isSelected = selectedTime == timeSlot.startTime // Supondo que o objeto `timeSlot` tenha a propriedade `time`

                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .clickable {
                                    if (isSelected) {
                                        appointmentViewModel.selectTime("Selecione um horário")
                                    } else {
                                        appointmentViewModel.selectTime(timeSlot.startTime) // Supondo que `timeSlot` tenha a propriedade `time`
                                    }
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
                                    text = timeSlot.startTime, // Aqui também
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

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

                // Botão de Continuar
                Button(
                    onClick = {
                        if (appointmentViewModel.canContinue()) {
                            appointmentViewModel.postAppointment(ProfessionalId, ServiceId, context)
                            navController.navigate("nextScreen") // Navegar para a próxima tela após o agendamento
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (appointmentViewModel.canContinue()) MaterialTheme.colorScheme.primary else Color(0xFF999999),
                    ),
                    enabled = appointmentViewModel.canContinue()
                ) {
                    Text("Continuar")
                }
            }
        }
    }
}




