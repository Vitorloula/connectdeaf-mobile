package com.connectdeaf.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.connectdeaf.MainActivity

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "appointment_channel",
            "Appointment Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificações de lembrete de agendamento"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun initializeNotifications(activity: ComponentActivity) {
    requestNotificationPermission(activity)
    createNotificationChannel(activity)
}

fun requestNotificationPermission(activity: ComponentActivity) {
    val requestPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onPermissionGranted(activity)
            } else {
                onPermissionDenied(activity)
            }
        }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onPermissionGranted(activity) // Permissão já foi concedida anteriormente
        }
    } else {
        onPermissionGranted(activity) // Permissões de notificação não são necessárias em versões mais antigas
    }
}

fun onPermissionGranted(context: Context) {
    //Toast.makeText(context, "Permissão para notificações concedida!", Toast.LENGTH_SHORT).show()
    // Aqui você pode adicionar lógica adicional caso necessário
}

fun onPermissionDenied(context: Context) {
    //Toast.makeText(context, "Permissão para notificações negada.", Toast.LENGTH_SHORT).show()
    // Aqui você pode adicionar lógica adicional caso necessário
}

