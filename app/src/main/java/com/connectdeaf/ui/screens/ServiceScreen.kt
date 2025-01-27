package com.connectdeaf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.connectdeaf.R
import com.connectdeaf.viewmodel.DrawerViewModel
import kotlinx.coroutines.launch

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.connectdeaf.ui.components.AssessmentCard
import com.connectdeaf.viewmodel.ServiceViewModel

@Composable
fun ServiceScreen(
    navController: NavController,
    serviceId: String,
    viewModel: ServiceViewModel = viewModel(),
    drawerViewModel: DrawerViewModel = viewModel()
) {
    val serviceState = viewModel.serviceState.collectAsState()
    val assessmentState = viewModel.serviceAssessment.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val category: List<String> = listOf("Categoria 1", "Categoria 2")

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
                // Card do Perfil
                CardProfile(
                    name = service.professional.name,
                    city = service.professional.addresses[0].city,
                    state = service.professional.addresses[0].state,
                    description = "Eu sou um profissional muito bom",
                    imageUrl = null,
                    professionalId = service.professional.id,
                    serviceId = service.id,
                    navController = navController
                )

                // Detalhes do Serviço
                CardDetailService(
                    title = service.name,
                    description = service.description,
                    category = category
                )

                SectionCardService(title = "Avaliações sobre o serviço") {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState()),
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
        } ?: run {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CardProfile(
    name: String,
    city: String,
    state: String,
    description: String,
    imageUrl: String?,
    professionalId: String,
    serviceId: String,
    navController: NavController
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
            // Avatar
            Image(
                painter = if (imageUrl.isNullOrEmpty()) painterResource(id = R.drawable.ic_user_placeholder)
                else rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Gray, shape = CircleShape)
                    .padding(8.dp)
            )

            // Informações do perfil
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
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botões
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { /* Lógica para agendar serviço */ }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Agendar Serviço")
                }
            }
        }
    }
}

@Composable
fun CardDetailService(title: String, description: String, category: List<String>) {
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
            // Imagem do Serviço
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                val imagePainter = rememberAsyncImagePainter("https://picsum.photos/800/600")
                Image(
                    painter = imagePainter,
                    contentDescription = "Service Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Título e Detalhes do Serviço
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            if (category.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    category.forEach { cat ->
                        ChipService(label = cat)
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Sobre esse serviço:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
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
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            content()
        }
    }
}

@Composable
fun ChipService(label: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


