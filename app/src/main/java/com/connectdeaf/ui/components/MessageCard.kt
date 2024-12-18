package com.connectdeaf.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.connectdeaf.R

@Composable
fun MessageCard(sender: String, message: String, isBot: Boolean) {
    val backgroundColor = if (isBot) Color(0xFFE2E8F7) else Color(0xFF3D66CC)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = if (isBot) Arrangement.Start else Arrangement.End // Alinha à esquerda para o bot, à direita para o usuário
    ) {
        if (isBot) {
            // Imagem do bot à esquerda
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Bot Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .padding(8.dp)
            )
        }

        // Espaço entre a imagem e a mensagem
        Spacer(modifier = Modifier.width(8.dp))

        // Coluna que contém o texto da mensagem
        Column(
            modifier = Modifier
                .background(backgroundColor, shape = MaterialTheme.shapes.medium)
                .padding(12.dp)
        ) {
            Text(
                text = sender,
                fontSize = 12.sp,
                color = if(isBot) Color.Gray else Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Start,
                color = if(isBot) Color.Gray else Color.White
            )
        }

        if (!isBot) {
            // Imagem do usuário à direita
            Spacer(modifier = Modifier.width(8.dp)) // Espaço à esquerda da imagem do usuário
            Image(
                painter = painterResource(id = R.drawable.avatar_user),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .padding(8.dp)
            )
        }
    }
}

