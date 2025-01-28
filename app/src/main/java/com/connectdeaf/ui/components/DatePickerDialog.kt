package com.connectdeaf.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onDismissRequest: () -> Unit, onDateSelected: (Int, Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Estado para o DatePicker
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis
    )

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Selecione a data", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(16.dp))

                // Exibe o DatePicker
                DatePicker(state = datePickerState)

                Spacer(modifier = Modifier.height(16.dp))

                // Botões de Cancelar e Confirmar
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismissRequest) {
                        Text(text = "Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        val date = Calendar.getInstance().apply {
                            timeInMillis = selectedDateMillis ?: System.currentTimeMillis()
                        }
                        onDateSelected(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(
                            Calendar.DAY_OF_MONTH))
                    }) {
                        Text(text = "Confirmar")
                    }
                }
            }
        }
    }
}