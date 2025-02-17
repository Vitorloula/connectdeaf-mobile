package com.connectdeaf.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.connectdeaf.data.model.Notification
import com.connectdeaf.data.repository.AuthRepository
import com.connectdeaf.network.dtos.AppointmentRequest
import com.connectdeaf.network.dtos.TimeSlotRequest
import com.connectdeaf.network.retrofit.ApiServiceFactory
import com.connectdeaf.utils.AppointmentNotificationWorker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class AppointmentViewModel : ViewModel() {
    var selectedDate = mutableStateOf("Selecione uma data")
        private set

    var selectedTime = mutableStateOf("Selecione um horário")
        private set

    var notifications = mutableStateListOf<Notification>()
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
                val apiServiceFactory = ApiServiceFactory(context)
                val professionalService = apiServiceFactory.professionalService


                val timeSlotRequest = professionalService.fetchTimeSlots(professionalId, date)


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

            val appointmentDateTime = dateTimeFormat.parse("$selectedDate $selectedTime")

            if (appointmentDateTime != null) {
                val appointmentTimeInMillis = appointmentDateTime.time
                val notificationTimeInMillis =
                    appointmentTimeInMillis - TimeUnit.HOURS.toMillis(4)

                return maxOf(
                    notificationTimeInMillis - currentDateTime.time,
                    0
                )
            }
        } catch (e: Exception) {
            println("Erro ao calcular o atraso da notificação: $e")
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
                    val appointmentRequest = AppointmentRequest(
                        date = selectedDate.value,
                        time = selectedTime.value
                    )

                    appointmentService.postAppointment(
                        userId,
                        professionalId,
                        serviceId,
                        appointmentRequest
                    )

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val delay =
                            calculateNotificationDelay(selectedDate.value, selectedTime.value)

                        val workRequest =
                            OneTimeWorkRequestBuilder<AppointmentNotificationWorker>()
                                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                .setInputData(
                                    workDataOf(
                                        "title" to "Lembrete de Agendamento",
                                        "message" to "Seu compromisso está marcado para ${selectedDate.value} às ${selectedTime.value}!"
                                    )
                                )
                                .build()

                        WorkManager.getInstance(context).enqueue(workRequest)

                        notifications.add(
                            Notification(
                                title = "Lembrete de Agendamento",
                                message = "Seu compromisso está marcado para ${selectedDate.value} às ${selectedTime.value}!",
                                time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                                icon = Icons.Default.Notifications
                            )
                        )

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