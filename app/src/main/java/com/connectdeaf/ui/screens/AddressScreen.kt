package com.connectdeaf.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.connectdeaf.viewmodel.AddressUiState
import com.connectdeaf.viewmodel.AddressViewModel

@Composable
fun AddressScreen(
    addressViewModel: AddressViewModel = viewModel(),
    onContinueClick: () -> Unit
) {
    val uiState by addressViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            com.connectdeaf.ui.components.TopAppBar(
                navController = null,
                showBackButton = true
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddressHeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            AddressInputFields(
                uiState = uiState,
                onCepChange = addressViewModel::onCepChange,
                onStateChange = addressViewModel::onStateChange,
                onCityChange = addressViewModel::onCityChange,
                onStreetChange = addressViewModel::onStreetChange,
                onNeighborhoodChange = addressViewModel::onNeighborhoodChange
            )

            Spacer(modifier = Modifier.weight(1f))

            Button (
                onClick = onContinueClick,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                enabled = uiState.isInputValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isInputValid) Color(0xFF478FCC) else Color(0xFF999999),
                    contentColor = if (uiState.isInputValid) Color.White else Color(0xFF478FCC)
                ),
                shape = RoundedCornerShape(6.dp),
            ) {
                Text(
                    "CONTINUAR",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun AddressHeaderSection() {
    Text(
        text = "Endereço",
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        color = Color.Black,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Nos conte onde podemos ajudar você!",
        fontSize = 16.sp,
        fontFamily = FontFamily.SansSerif,
        color = Color.Black,
    )
}

@Composable
fun AddressInputFields(
    uiState: AddressUiState,
    onCepChange: (String) -> Unit,
    onStateChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onStreetChange: (String) -> Unit,
    onNeighborhoodChange: (String) -> Unit
) {
    OutlinedTextField(
        value = uiState.cep,
        onValueChange = onCepChange,
        label = { Text("CEP") },
        shape = RoundedCornerShape(5.dp),
        textStyle = TextStyle(color = Color.Black),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row {
        OutlinedTextField(
            value = uiState.state,
            onValueChange = onStateChange,
            label = { Text("Estado") },
            shape = RoundedCornerShape(5.dp),
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier.width(116.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = uiState.city,
            onValueChange = onCityChange,
            label = { Text("Cidade") },
            shape = RoundedCornerShape(5.dp),
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = uiState.street,
        onValueChange = onStreetChange,
        label = { Text("Rua") },
        shape = RoundedCornerShape(5.dp),
        textStyle = TextStyle(color = Color.Black),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = uiState.neighborhood,
        onValueChange = onNeighborhoodChange,
        label = { Text("Bairro") },
        shape = RoundedCornerShape(5.dp),
        textStyle = TextStyle(color = Color.Black),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun PreviewAddressScreen() {
    AddressScreen(onContinueClick = {})
}