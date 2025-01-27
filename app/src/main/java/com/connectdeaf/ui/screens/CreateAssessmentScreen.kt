package com.connectdeaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.connectdeaf.data.repository.AuthRepository
import com.connectdeaf.viewmodel.AssessmentViewModel
import com.connectdeaf.viewmodel.factory.AssessmentViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAssessmentScreen(
    navController: NavController,
    serviceId: String,
    viewModel: AssessmentViewModel = viewModel(factory = AssessmentViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val authRepository = AuthRepository(context)
    val userId = authRepository.getUserId()

    val professionalId by viewModel.professionalId.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    var text by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }

    LaunchedEffect(serviceId) {
        viewModel.fetchProfessionalId(serviceId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Avaliação") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Avalie o serviço",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Comentário") },
                    modifier = Modifier.fillMaxWidth()
                )

                Slider(
                    value = rating.toFloat(),
                    onValueChange = { rating = it.toInt() },
                    valueRange = 0f..5f,
                    steps = 4,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Nota: $rating", style = MaterialTheme.typography.bodyMedium)

                Button(
                    onClick = {
                        if (userId != null && professionalId != null) {
                            viewModel.createAssessment(
                                text = text,
                                rating = rating,
                                userId = userId,
                                professionalId = professionalId!!,
                                serviceId = serviceId
                            )
                        }
                    },
                    enabled = professionalId != null && text.isNotBlank() && rating > 0
                ) {
                    Text("Enviar Avaliação")
                }

                if (message.isNotBlank()) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
