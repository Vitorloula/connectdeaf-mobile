package com.connectdeaf.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.connectdeaf.viewmodel.Appointment
import com.connectdeaf.viewmodel.calculateMonthlyRevenue

@Composable
fun MonthlyRevenueChart(appointments: List<Appointment>) {
    val monthlyRevenue = calculateMonthlyRevenue(appointments)

    val chartHeight = 200.dp
    val chartWidth = 300.dp
    val barWidth = 40.dp
    val padding = 16.dp

    val maxRevenue = monthlyRevenue.maxOfOrNull { it.revenue } ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rendimento Mensal",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(chartHeight)
                .background(Color.White)
        ) {
            // Desenha os eixos
            drawLine(
                color = Color.Gray,
                start = androidx.compose.ui.geometry.Offset(0f, size.height),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                strokeWidth = 3f
            )

            drawLine(
                color = Color.Gray,
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(0f, size.height),
                strokeWidth = 3f
            )

            // Desenha as barras
            val spacing = size.width / (monthlyRevenue.size + 1)
            monthlyRevenue.forEachIndexed { index, data ->
                val barHeight = if (maxRevenue > 0) (data.revenue / maxRevenue).toFloat() * size.height else 0f

                translate(left = spacing * (index + 1), top = size.height - barHeight) {
                    drawRoundRect(
                        color = Color.Blue,
                        size = androidx.compose.ui.geometry.Size(barWidth.toPx(), barHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                    )
                }

                // Exibe os valores acima das barras
                drawContext.canvas.nativeCanvas.drawText(
                    "R$ ${"%.2f".format(data.revenue)}",
                    spacing * (index + 1) + barWidth.toPx() / 4,
                    size.height - barHeight - 8,
                    android.graphics.Paint().apply {
                        textSize = 28f
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }

        // Exibe os meses abaixo das barras
        Row(
            modifier = Modifier
                .width(chartWidth)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            monthlyRevenue.forEach { data ->
                Text(
                    text = data.month,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}
