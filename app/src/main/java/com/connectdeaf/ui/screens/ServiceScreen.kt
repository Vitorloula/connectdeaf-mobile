package com.connectdeaf.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.connectdeaf.R
import com.connectdeaf.ui.components.AssessmentCard
import com.connectdeaf.viewmodel.DrawerViewModel
import com.connectdeaf.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch

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
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        assessmentState.value?.forEach { assessment ->
                            AssessmentCard(
                                name = assessment.userName,
                                stars = assessment.rating,
                                description = assessment.text
                            )
                        }
                    }
                }
            }
        } ?: CircularProgressIndicator()
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
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = if (imageUrl.isNullOrEmpty()) painterResource(id = R.drawable.ic_user_placeholder)
                else rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Gray, CircleShape)
                    .padding(8.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$city - $state",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = description, style = MaterialTheme.typography.bodySmall)
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
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://picsum.photos/800/600"),
                contentDescription = "Service Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                category.forEach { ChipService(label = it) }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Sobre esse serviço:", style = MaterialTheme.typography.bodyLarge)
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )

            {
                Button(
                    onClick = { navController.navigate("appointmentScreen/${serviceId}/${professionalId}/${value}") },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Agendar")
                }
            }
        }
    }
}

@Composable
fun SectionCardService(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
fun ChipService(label: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
            .padding(8.dp)
    ) {
        Text(text = label, color = Color.White, style = MaterialTheme.typography.bodySmall)
    }
}
