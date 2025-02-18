package com.connectdeaf.ui.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.connectdeaf.R
import com.connectdeaf.data.repository.AuthRepository
import com.connectdeaf.ui.theme.ErrorColor
import com.connectdeaf.ui.theme.PrimaryColor

@Composable
fun ServiceCard(
    id: String,
    name: String,
    description: String,
    image: String? = null,
    value: Double,
    onClick: (id: String) -> Unit,
    isProfessional: Boolean = false,
    onDeleteClick: (id: String) -> Unit = {},
    onEditClick: (id: String) -> Unit = {}

) {
    val context = LocalContext.current
    val authRepository = AuthRepository(context)
    val userRoles = authRepository.getRoles()

    Card(
        modifier = Modifier
            .clickable { onClick(id.toString()) }
            .fillMaxWidth()
            .widthIn(max = 180.dp)
            .background(Color.White)
            .height(218.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                if (image == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                } else {
                    Image(
                        painter = painterResource( id = R.drawable.doutor),
                        contentDescription = "Service Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Value and Description
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = name, fontSize = 16.sp, color = Color.Black)
                Text(text = "A partir de $value", fontSize = 14.sp, color = Color.Black)
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (userRoles != null) {
                if (userRoles.contains("ROLE_PROFESSIONAL")) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onEditClick(id) }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Editar",
                                tint = PrimaryColor,

                                )
                        }
                        IconButton(onClick = { onDeleteClick(id) }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Deletar",
                                tint = ErrorColor,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
