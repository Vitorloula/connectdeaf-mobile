
package com.connectdeaf.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.connectdeaf.R

@Composable
fun AssessmentCard(
    name: String,
    stars: Int,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // User Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Gray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.firstOrNull()?.toString()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                }
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
            }


            Spacer(modifier = Modifier.height(2.dp))

            // Stars
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(5) { index ->
                    Image(
                        painter = painterResource(id = if (index < stars) R.drawable.star_filled_icon else R.drawable.star_icon),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "$stars estrelas",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                    color = Color.Gray
                )
            }


            Spacer(modifier = Modifier.height(2.dp))

            // Description
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun AssessmentCardPreview() {
    AssessmentCard(
        name = "João Silva",
        stars = 4,
        description = "Ótimo profissional, muito atencioso. Super recomendo!"
    )
}
