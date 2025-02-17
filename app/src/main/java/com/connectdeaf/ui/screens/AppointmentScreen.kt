package com.connectdeaf.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.connectdeaf.network.dtos.PaymentRequest
import com.connectdeaf.network.retrofit.ApiServiceFactory
import com.connectdeaf.ui.components.DatePickerDialog
import com.connectdeaf.ui.components.DrawerMenu
import com.connectdeaf.viewmodel.DrawerViewModel
import com.connectdeaf.viewmodel.AppointmentViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException

@Composable
fun AppointmentScreen(
    navController: NavController,
    drawerViewModel: DrawerViewModel = viewModel(),
    appointmentViewModel: AppointmentViewModel = viewModel(),
    serviceId: String,
    professionalId: String,
    value: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val selectedDate = appointmentViewModel.selectedDate.value
    val selectedTime = appointmentViewModel.selectedTime.value
    val showDatePickerDialog = remember { mutableStateOf(false) }

    val availableTimes = appointmentViewModel.availableTimeSlots

    var isLoading by remember { mutableStateOf(false) }
    val apiServiceFactory = remember { ApiServiceFactory(context) }

    LaunchedEffect(selectedDate) {
        if (selectedDate != "Selecione uma data") {
            appointmentViewModel.fetchTimeSlots(professionalId, selectedDate, context)
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

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Selecione uma data e horário disponível para o serviço",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(32.dp))

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
                    items(availableTimes.value) { timeSlot ->
                        val isSelected = selectedTime == timeSlot.startTime

                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .clickable {
                                    if (isSelected) {
                                        appointmentViewModel.selectTime("Selecione um horário")
                                    } else {
                                        appointmentViewModel.selectTime(timeSlot.startTime)
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
                                    text = timeSlot.startTime,
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

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        if (appointmentViewModel.canContinue() && !isLoading) {
                            scope.launch {
                                isLoading = true
                                try {
                                    if (value.isEmpty() || value.toBigDecimalOrNull() == null) {
                                        Toast.makeText(context, "Valor inválido", Toast.LENGTH_SHORT).show()
                                        return@launch
                                    }

                                    val paymentRequest = PaymentRequest(
                                        amount = value.toBigDecimal(),
                                        email = "vitor@gmail.com"
                                    )

                                    val retrofitResponse =
                                        apiServiceFactory.paymentService.createPixPayment(paymentRequest)

                                    if (retrofitResponse.isSuccessful) {
                                        val paymentResponse = retrofitResponse.body()

                                        if (paymentResponse != null) {
                                            val paymentLink = paymentResponse.paymentLink

                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentLink))
                                            startActivity(context, intent, null)
                                            Toast.makeText(context, "Pagamento iniciado com sucesso!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Resposta vazia do servidor", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "Erro no pagamento", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: IOException) {
                                    Toast.makeText(context, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                                } catch (e: HttpException) {
                                    Toast.makeText(context, "Erro no servidor. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Erro inesperado: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (appointmentViewModel.canContinue() && !isLoading) MaterialTheme.colorScheme.primary else Color(0xFF999999),
                    ),
                    enabled = appointmentViewModel.canContinue() && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text("Continuar")
                    }
                }
            }
        }
    }
}