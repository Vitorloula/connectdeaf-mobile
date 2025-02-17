package com.connectdeaf.viewmodel

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.connectdeaf.data.model.Notification
import com.connectdeaf.domain.model.ScheduleItem
import com.connectdeaf.utils.AppointmentNotificationWorker
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class NotificationViewModel : ViewModel() {

    private val _notifications = mutableStateListOf<Notification>()


    var formatedTime: String? = null


    fun createNotification(scheduleItem: ScheduleItem): Notification {
        val title = "Lembrete de Agendamento: ${scheduleItem.serviceName}"
        val message = "Fique tranquilo, vamos lembrar você do seu compromisso com ${scheduleItem.professionalName} na data: ${scheduleItem.date} às ${scheduleItem.startTime}."


        val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))


        formatedTime = scheduleItem.date + " " + scheduleItem.startTime.removeRange(
            scheduleItem.startTime.length - 3,
            scheduleItem.startTime.length
        ).trim()

        val notification = Notification(
            title = title,
            message = message,
            time = currentTime
        )


        _notifications.add(notification)

        Log.d("NotificationViewModel", "Notificação criada: $notification")
        Log.d("NotificationViewModel", "Lista de notificações: $_notifications")

        return notification
    }

    fun getNotifications(): List<Notification> {
        return _notifications
    }


    fun calculateNotificationTime(startTime: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        if (formatedTime.isNullOrEmpty()) {
            throw IllegalArgumentException("formatedTime não foi configurado corretamente.")
        }

        val startDateTime = LocalDateTime.parse(formatedTime, formatter)


        val notificationTime = startDateTime.minus(Duration.ofHours(4))

        return notificationTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
    }


    fun sendNotification(context: Context, notification: Notification) {
        val workManager = WorkManager.getInstance(context)

        val inputData = workDataOf(
            "title" to notification.title,
            "message" to notification.message
        )


        val notificationTimeMillis = calculateNotificationTime(notification.time)

        val notificationWorkRequest = OneTimeWorkRequestBuilder<AppointmentNotificationWorker>()
            .setInputData(inputData)
            .setInitialDelay(notificationTimeMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueue(notificationWorkRequest)

        val confirmationNotification = Notification(
            title = "Notificação Agendada",
            message = "Fique tranquilo vamos lembrar voce do seu compromisso.",
            time = notification.time
        )

        sendConfirmationNotification(context, confirmationNotification)
    }

    private fun sendConfirmationNotification(context: Context, notification: Notification) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, "appointment_channel")
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)

        notificationManager.notify(1, builder.build())

        Log.d("NotificationViewModel", "Notificação de confirmação enviada: $notification")
    }
}





