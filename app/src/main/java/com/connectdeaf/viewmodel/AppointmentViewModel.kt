package com.connectdeaf.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.connectdeaf.data.repository.AuthRepository
import com.connectdeaf.network.dtos.AppointmentRequest
import com.connectdeaf.network.dtos.TimeSlotRequest
import com.connectdeaf.network.retrofit.ApiServiceFactory
import com.connectdeaf.utils.AppointmentNotificationWorker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


    class AppointmentViewModel : ViewModel() {
        var selectedDate = mutableStateOf("Selecione uma data")
            private set

        var selectedTime = mutableStateOf("Selecione um horário")
            private set

        var availableTimeSlots = mutableStateOf<List<TimeSlotRequest>>(emptyList())

        fun selectDate(date: String) {
            selectedDate.value = date
        }

        fun selectTime(time: String) {
            selectedTime.value = time
        }

        fun canContinue(): Boolean {
            return selectedDate.value != "Selecione uma data" && selectedTime.value != "Selecione um horário"
        }

        fun fetchTimeSlots(professionalId: String, date: String, context: Context) {
            viewModelScope.launch {
                try {
                    val apiServiceFactory = ApiServiceFactory(context)  // Supondo que você tenha esse método
                    val professionalService = apiServiceFactory.professionalService

                    // Chama a API para buscar os horários disponíveis de forma assíncrona
                    val timeSlotRequest = professionalService.fetchTimeSlots(professionalId, date)

                    // Verifica se o resultado foi bem-sucedido
                    availableTimeSlots.value = timeSlotRequest
                } catch (e: Exception) {
                    println("Error: $e")
                }
            }
        }

        fun calculateNotificationDelay(selectedDate: String, selectedTime: String): Long {
            val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val currentDateTime = Calendar.getInstance().time

            try {
                // Combina a data e hora selecionadas em um único objeto Date
                val appointmentDateTime = dateTimeFormat.parse("$selectedDate $selectedTime")

                if (appointmentDateTime != null) {
                    val appointmentTimeInMillis = appointmentDateTime.time
                    val notificationTimeInMillis = appointmentTimeInMillis - TimeUnit.HOURS.toMillis(4) // 4 horas antes

                    return maxOf(notificationTimeInMillis - currentDateTime.time, 0) // Evita valores negativos
                }
            } catch (e: Exception) {
                println("Erro ao calcular o atraso da notificação: $e")
            }
            return 0L // Retorna 0 como fallback
        }


            fun postAppointment(professionalId: String, serviceId: String, context: Context) {
                val authRepository = AuthRepository(context)
                val userId = authRepository.getUserId()

                viewModelScope.launch {
                    try {
                        val apiServiceFactory = ApiServiceFactory(context)
                        val appointmentService = apiServiceFactory.appointmentService
                        if (userId != null) {
                            val appointmentRequest = AppointmentRequest(
                                date = selectedDate.value,
                                time = selectedTime.value
                            )
                            // Envia a requisição de agendamento
                            appointmentService.postAppointment(userId, professionalId, serviceId, appointmentRequest)

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                            ) {
                                val delay = calculateNotificationDelay(selectedDate.value, selectedTime.value)

                                // Configura o WorkManager para disparar a notificação
                                val workRequest = OneTimeWorkRequestBuilder<AppointmentNotificationWorker>()
                                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                    .setInputData(
                                        workDataOf(
                                            "title" to "Lembrete de Agendamento",
                                            "message" to "Seu compromisso está marcado para ${selectedDate.value} às ${selectedTime.value}!"
                                        )
                                    )
                                    .build()

                                WorkManager.getInstance(context).enqueue(workRequest)
                            } else {
                                println("Permissão para notificações não concedida. Notificação não configurada.")
                            }
                        }
                    } catch (e: Exception) {
                        println("Error: $e")
                    }
                }
            }
        }
