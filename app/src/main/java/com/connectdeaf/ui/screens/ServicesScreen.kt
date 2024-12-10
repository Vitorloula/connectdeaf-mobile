package com.connectdeaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.connectdeaf.ui.components.DropdownMenuField
import com.connectdeaf.ui.components.SearchBarField
import com.connectdeaf.ui.components.ServiceCard
import com.connectdeaf.viewmodel.Service

@Composable
fun ServicesScreen(serviceList: List<Service>) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var currentPage by remember { mutableStateOf(0) }
    val servicesPerPage = 10

    val CustomBlue = Color(0xFF3D66CC)

    val gridState = rememberLazyGridState()

    val paginatedList by remember(serviceList, currentPage) {
        derivedStateOf {
            val startIndex = currentPage * servicesPerPage
            val endIndex = (startIndex + servicesPerPage).coerceAtMost(serviceList.size)
            serviceList.subList(startIndex, endIndex)
        }
    }

    LaunchedEffect(serviceList) {
        val maxPages = (serviceList.size - 1) / servicesPerPage
        if (currentPage > maxPages) currentPage = 0
    }

    LaunchedEffect(currentPage) {
        gridState.scrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Serviços",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Talentos cuidadosamente escolhidos para suprir suas demandas profissionais.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Search Bar
        SearchBarField (
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            modifier = Modifier.padding(bottom = 16.dp),
            placeholder = "Pesquisar por serviço..."
        )

        // Dropdown Menus
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropdownMenuField(
                label = "Estado",
                options = listOf("São Paulo", "Rio de Janeiro", "Bahia"),
                modifier = Modifier.weight(1f)
            )
            DropdownMenuField(
                label = "Cidade",
                options = listOf("Campinas", "Niterói", "Salvador"),
                modifier = Modifier.weight(1f)
            )
        }

        // Grid de serviços com paginação
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            items(paginatedList) { service ->
                ServiceCard(
                    id = service.id,
                    description = service.description,
                    image = service.imageUrl,
                    value = service.value,
                    onClick = { id -> println("Clicked on service with id: $id") }
                )
            }
        }

        // Botões de paginação
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomBlue, // Cor de fundo do botão
                    contentColor = Color.White   // Cor do texto dentro do botão
                )
            ) {
                Text("Anterior")
            }

            Text(
                text = "Página ${currentPage + 1} de ${((serviceList.size - 1) / servicesPerPage) + 1}",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = { if ((currentPage + 1) * servicesPerPage < serviceList.size) currentPage++ },
                enabled = (currentPage + 1) * servicesPerPage < serviceList.size,
                        colors = ButtonDefaults.buttonColors(
                        containerColor = CustomBlue, // Cor de fundo do botão
                        contentColor = Color.White   // Cor do texto dentro do botão
            )
            ) {
                Text("Próximo")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServicesScreenPreview() {
    val mockServiceList = List(25) { index ->
        Service(
            id = "service_$index",
            name = "Serviço ${index + 1}",
            description = "Descrição do Serviço ${index + 1}",
            category = listOf("Categoria A", "Categoria B"),
            value = "R$ ${(100 + index * 10)}",
            imageUrl = "https://via.placeholder.com/150" // URL mock para imagens
        )
    }

    ServicesScreen(serviceList = mockServiceList)
}
