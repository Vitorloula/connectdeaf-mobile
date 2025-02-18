package com.connectdeaf.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.connectdeaf.R
import com.connectdeaf.ui.components.AssessmentCard
import com.connectdeaf.viewmodel.DrawerViewModel
import com.connectdeaf.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ServiceScreen(
    navController: NavController,
    serviceId: String,
    viewModel: ServiceViewModel = viewModel(),
    drawerViewModel: DrawerViewModel = viewModel(),
) {
    val serviceState = viewModel.serviceState.collectAsState()
    val assessmentState = viewModel.serviceAssessment.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val category = listOf("Categoria 1", "Categoria 2")

    LaunchedEffect(serviceId) {
        viewModel.fetchServiceDetail(serviceId, context)
        viewModel.fetchServiceAssessment(serviceId, context)
    }

    Scaffold(
        topBar = {
            com.connectdeaf.ui.components.TopAppBar(
                onOpenDrawerMenu = { scope.launch { drawerViewModel.openMenuDrawer() } },
                onOpenDrawerNotifications = { scope.launch { drawerViewModel.openNotificationsDrawer() } },
                showBackButton = true,
                navController = navController
            )
        }
    ) { paddingValues ->
        serviceState.value?.let { service ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardProfile(
                    name = service.professional.name,
                    city = service.professional.addresses[0].city,
                    state = service.professional.addresses[0].state,
                    description = "Eu sou um profissional muito bom",
                    imageUrl = null
                )

                CardDetailService(
                    title = service.name,
                    description = service.description,
                    category = category,
                    value = service.value.toString(),
                    navController = navController,
                    professionalId = service.professional.id,
                    serviceId = service.id
                )

                SectionCardService(title = "Avaliações sobre o serviço") {
                    when {
                        assessmentState.value == null -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        assessmentState.value!!.isEmpty() -> {
                            Text(
                                text = "Ainda não há avaliações.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }

                        else -> {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(assessmentState.value!!) { assessment ->
                                    AssessmentCard(
                                        name = assessment.userName,
                                        stars = assessment.rating,
                                        description = assessment.text,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardProfile(
    name: String, city: String, state: String, description: String,
    imageUrl: String?,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape) // Borda colorida
                    .padding(4.dp)
            ) {
                Image(
                    painter = if (imageUrl.isNullOrEmpty()) painterResource(id = R.drawable.ic_user_placeholder)
                    else rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp), // Espaçamento reduzido
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), // Texto em negrito
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$city - $state",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant), // Cor mais suave
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant), // Cor mais suave
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CardDetailService(
    title: String, description: String, category: List<String>,
    value: String,
    navController: NavController,
    professionalId: String, serviceId: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)) // Bordas arredondadas para a imagem
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https://picsum.photos/800/600"),
                    contentDescription = "Service Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)))) // Overlay escuro
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                category.forEach { ChipService(label = it) }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Sobre esse serviço:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }

            Button(
                onClick = { navController.navigate("appointmentScreen/${serviceId}/${professionalId}/${value}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) // Cor destacada
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Agendar", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun SectionCardService(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.star_filled_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold) // Texto em negrito
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun ChipService(label: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
