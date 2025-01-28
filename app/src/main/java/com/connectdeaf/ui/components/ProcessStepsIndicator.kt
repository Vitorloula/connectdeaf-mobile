package com.connectdeaf.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProcessStepsIndicator(
    currentStep: Int,
    totalSteps: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        for (i in 1..totalSteps) {
            val isCurrentStep = i == currentStep
            val isCompleted = i < currentStep

            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .width(2.dp)
                    .height(2.dp)
                    .background(
                        color = when {
                            isCompleted -> Color.Yellow // Etapa concluída
                            isCurrentStep -> Color.Yellow // Etapa atual
                            else -> Color.Gray // Etapas futuras
                        },
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}