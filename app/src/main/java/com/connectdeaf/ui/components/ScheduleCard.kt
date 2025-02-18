package com.connectdeaf.ui.components

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.sharp.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.connectdeaf.domain.model.ScheduleItem
import com.connectdeaf.viewmodel.NotificationViewModel
import com.connectdeaf.viewmodel.ScheduleViewModel

@Composable
fun ScheduleCard(
    navController: NavController,
    schedule: ScheduleItem,
    role: String,
    scheduleViewModel: ScheduleViewModel,
    notificationViewModel: NotificationViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var actionType by remember { mutableStateOf("") }

    var status by remember { mutableStateOf(schedule.status) }
    var statusColor by remember { mutableStateOf(schedule.statusColor) }

    val cardHeight by animateDpAsState(targetValue = if (expanded) 240.dp else 190.dp, label = "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            if (role == "[ROLE_PROFESSIONAL]" && schedule.status == "Pendente") {
                expanded = !expanded
            }

            if (role == "[ROLE_USER]") {
                navController.navigate("createAssessmentScreen/${schedule.serviceId}")
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = schedule.serviceName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                if (schedule.status == "Rejeitado") {
                    IconButton(onClick = {
                        actionType = "delete"
                        showConfirmationDialog = true

                    }, modifier = Modifier.size(20.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Notifications Icon",
                            tint = Color.Black
                        )
                    }
                } else if (schedule.status == "Aprovado") {
                    IconButton(onClick = {
                        notificationViewModel.sendNotification(context, notificationViewModel.createNotification(
                            schedule
                        ))
                    }, modifier = Modifier.size(20.dp)) {
                        Icon(
                            imageVector = Icons.Sharp.Notifications,
                            contentDescription = "Notifications Icon",
                            tint = Color.Black
                        )
                    }
                } else {
                    IconButton(onClick = {
                        if (role == "[ROLE_PROFESSIONAL]") {
                            Toast.makeText(context, "Aceite ou Rejeite o pedido antes de marca um lembrete", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Aguarde a confirmação para marcar um lembrete", Toast.LENGTH_SHORT).show()

                        }


                    }, modifier = Modifier.size(20.dp)) {
                        Icon(
                            imageVector = Icons.Sharp.Notifications,
                            contentDescription = "Notifications Icon",
                            tint = Color.Black,
                        )
                    }
                }
            }




            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Client Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = schedule.clientName,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = schedule.date,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    var statusFilter = status
                    var statusColorFilter = statusColor

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Status Icon",
                        tint = statusColor, //if (isFiltered) statusColorFilter else statusColor,  // Usando a co r reativa
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = status, //if (isFiltered) statusFilter else status,
                        fontSize = 14.sp,
                        color = statusColor,  //if (isFiltered) statusColorFilter else Color.Black,  // Usando a cor reativa
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "time Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = (
                                "Horário: " +
                                        schedule.startTime.removeRange(
                                            schedule.startTime.length - 3,
                                            schedule.startTime.length
                                        )
                                ),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Professional Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = schedule.professionalName,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }

                schedule.address.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Location Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        IconButton(
                            onClick = {
                                showConfirmationDialog = true
                                actionType = "accept"
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Accept Icon",
                                tint = Color.Green
                            )
                        }

                        IconButton(
                            onClick = {
                                showConfirmationDialog = true
                                actionType = "reject"
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Reject Icon",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = {
                Text(text = "Confirmação")
            },
            text = {
                Text(
                    text = if (actionType == "accept") {
                        "Você tem certeza que deseja aceitar este agendamento?"
                    } else if (actionType == "reject") {
                        "Você tem certeza que deseja recusar este agendamento?"
                    } else {
                        "Você tem certeza que deseja excluir este agendamento?"
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        if (actionType == "accept") {
                            scheduleViewModel.acceptAppointment(
                                appointmentId = schedule.appointmentId.toString(),
                                context = context
                            )

                            status = "Aprovado"
                            statusColor = Color.Green
                            showConfirmationDialog = false
                            expanded = false
                            scheduleViewModel.filterSchedules()


                        } else if (actionType == "reject") {
                            scheduleViewModel.rejectAppointment(
                                appointmentId = schedule.appointmentId.toString(),
                                context = context
                            )


                            status = "Recusado"
                            statusColor = Color.Red
                            showConfirmationDialog = false
                            expanded = false
                            scheduleViewModel.filterSchedules()


                        } else if (actionType == "delete") {
                            scheduleViewModel.deleteAppointment(
                                appointmentId = schedule.appointmentId.toString(),
                                context = context
                            )

                            expanded = false
                            scheduleViewModel.filterSchedules()
                            navController.popBackStack()
                            navController.navigate("scheduleScreen")
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmationDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}