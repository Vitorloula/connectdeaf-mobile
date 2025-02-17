package com.connectdeaf.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
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
import java.util.Locale
import java.util.concurrent.TimeUnit

class AppointmentViewModel : ViewModel() {
    var selectedDate = mutableStateOf("Selecione uma data")
    var selectedTime = mutableStateOf("Selecione um horário")
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

    fun fetchTimeSlots(professionalId: String, rawDate: String, context: Context) {
        viewModelScope.launch {
            try {
                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val parsedDate = inputFormat.parse(rawDate)
                val formattedDate = outputFormat.format(parsedDate!!)

                val apiServiceFactory = ApiServiceFactory(context)
                val professionalService = apiServiceFactory.professionalService

                val response = professionalService.fetchTimeSlots(professionalId, formattedDate)

                if (response.isSuccessful) {
                    availableTimeSlots.value = response.body() ?: emptyList()
                } else {
                    Log.e("API Error", "Código: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Network Error", e.message ?: "Erro desconhecido")
            }
        }
    }

    private fun calculateNotificationDelay(selectedDate: String, selectedTime: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        try {
            val parsedDate = dateFormat.parse(selectedDate)
            val isoDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate!!)

            val appointmentDateTime = dateTimeFormat.parse("$isoDate $selectedTime")

            if (appointmentDateTime != null) {
                val appointmentTimeInMillis = appointmentDateTime.time
                val notificationTimeInMillis = appointmentTimeInMillis - TimeUnit.HOURS.toMillis(4)

                return maxOf(notificationTimeInMillis - System.currentTimeMillis(), 0)
            }
        } catch (e: Exception) {
            Log.e("Notification Error", "Erro ao calcular notificação: $e")
        }
        return 0L
    }

    fun postAppointment(professionalId: String, serviceId: String, context: Context) {
        val authRepository = AuthRepository(context)
        val userId = authRepository.getUserId()

        viewModelScope.launch {
            try {
                val apiServiceFactory = ApiServiceFactory(context)
                val appointmentService = apiServiceFactory.appointmentService
                if (userId != null) {
                    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val parsedDate = inputFormat.parse(selectedDate.value)
                    val formattedDate = outputFormat.format(parsedDate!!)

                    val timeSlot =
                        availableTimeSlots.value.find { it.startTime == selectedTime.value }
                    val endTime = timeSlot?.endTime

                    if (endTime == null) {
                        Log.e(
                            "AppointmentError",
                            "EndTime não encontrado para o horário selecionado."
                        )
                        return@launch
                    }

                    val appointmentRequest = AppointmentRequest(
                        customerId = userId,
                        professionalId = professionalId,
                        serviceId = serviceId,
                        date = formattedDate,
                        startTime = selectedTime.value,
                        endTime = endTime
                    )
                    appointmentService.postAppointment(
                        appointmentRequest
                    )
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val delay =
                            calculateNotificationDelay(selectedDate.value, selectedTime.value)
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
                        Log.w("Notification", "Permissão para notificações não concedida.")
                    }
                }
            } catch (e: Exception) {
                Log.e("Appointment Error", "Error: $e")
            }
        }
    }
}