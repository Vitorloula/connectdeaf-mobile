package com.connectdeaf.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ProfilePictureSection(
    onClick: () -> Unit,
    imageResourceId: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = imageResourceId),
            contentDescription = "Imagem De Perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(64.dp)
                .clip(CircleShape)
        )
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()

                .drawBehind {
                    val dashWidth = 6.dp.toPx() // tamanho do traço
                    val dashGap = 4.dp.toPx() // espaço entre os traçoss
                    val paint = android.graphics
                        .Paint()
                        .apply {
                            color = android.graphics.Color.parseColor("#478FCC")
                            style = android.graphics.Paint.Style.STROKE
                            strokeWidth = 2.dp.toPx()
                            pathEffect =
                                android.graphics.DashPathEffect(
                                    floatArrayOf(dashWidth, dashGap),
                                    0f
                                )
                        }
                    val path = android.graphics
                        .Path()
                        .apply {
                            addRoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height,
                                2.dp.toPx(),
                                2.dp.toPx(),
                                android.graphics.Path.Direction.CW
                            )
                        }
                    drawContext.canvas.nativeCanvas.drawPath(path, paint)
                },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF478FCC)
            ),
            border = BorderStroke(0.dp, Color.Transparent) // Remover a borda padrão
        ) {
            Text("Adicionar Foto De Perfil")

        }

    }
}