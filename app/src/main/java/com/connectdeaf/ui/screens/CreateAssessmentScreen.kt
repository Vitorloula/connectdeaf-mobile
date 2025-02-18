package com.connectdeaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.connectdeaf.data.repository.AuthRepository
import com.connectdeaf.viewmodel.AssessmentViewModel
import com.connectdeaf.viewmodel.factory.AssessmentViewModelFactory

@Composable
fun CreateAssessmentScreen(
    navController: NavController,
    serviceId: String,
    viewModel: AssessmentViewModel = viewModel(factory = AssessmentViewModelFactory(LocalContext.current)),
) {
    val context = LocalContext.current
    val authRepository = AuthRepository(context)
    val userId = authRepository.getUserId()

    val professionalId by viewModel.professionalId.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    var text by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(serviceId) {
        viewModel.fetchProfessionalId(serviceId)
    }

    LaunchedEffect(message) {
        if (message.startsWith("Sucesso")) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            com.connectdeaf.ui.components.TopAppBar(
                showBackButton = true,
                navController = navController
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
                    text = "Avalie o serviço e o profissional",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        isError = it.length > 500 // Limita o comentário a 500 caracteres
                    },
                    label = { Text("Comentário (máximo 500 caracteres)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            Text("O comentário deve ter no máximo 500 caracteres.")
                        }
                    }
                )

                Text("Nota: $rating", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = rating.toFloat(),
                    onValueChange = { rating = it.toInt() },
                    valueRange = 0f..5f,
                    steps = 4,
                    modifier = Modifier.fillMaxWidth()
                )

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
                    enabled = professionalId != null && text.isNotBlank() && rating > 0 && !isLoading
                ) {
                    Text("Enviar Avaliação")
                }

                if (message.isNotBlank()) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (message.startsWith("Sucesso")) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                }
            }
        }
    }
}
