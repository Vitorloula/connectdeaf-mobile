package com.connectdeaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.connectdeaf.ui.components.DrawerMenu
import com.connectdeaf.ui.components.DropdownMenuField
import com.connectdeaf.ui.components.SearchBarField
import com.connectdeaf.ui.components.ServiceCard
import com.connectdeaf.viewmodel.DrawerViewModel
import com.connectdeaf.ui.theme.AppStrings
import com.connectdeaf.viewmodel.ServicesViewModel
import com.connectdeaf.viewmodel.factory.ServicesViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun ServicesScreen(
    navController: NavController,
    drawerViewModel: DrawerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context = LocalContext.current
    val viewModel: ServicesViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ServicesViewModelFactory(context)
    )

    val isLoading = viewModel.isLoading.value
    val serviceList = viewModel.getPaginatedList()
    val searchQuery = viewModel.searchQuery.value

    val CustomBlue = Color(0xFF3D66CC)

    val scope = rememberCoroutineScope()

    DrawerMenu(
        navController = navController,
        scope = scope,
        drawerViewModel = drawerViewModel,
    ) {
        Scaffold(
            topBar = {
                com.connectdeaf.ui.components.TopAppBar(
                    onOpenDrawerMenu = { scope.launch { drawerViewModel.openMenuDrawer() } },
                    onOpenDrawerNotifications = { scope.launch { drawerViewModel.openNotificationsDrawer() } },
                    showBackButton = false,
                    navController = navController
                )
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CustomBlue)
                    }
                } else {
                    // Search Bar
                    SearchBarField(
                        searchQuery = TextFieldValue(searchQuery),
                        onSearchQueryChange = { viewModel.onSearchQueryChange(it.text) },
                        placeholder = "Pesquisar por serviço...",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Dropdown Menus
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DropdownMenuField(
                            value = viewModel.selectedState.value,
                            label = AppStrings.ESTADO,
                            onValueChange = { selectedState -> viewModel.updateState(selectedState) },
                            options = listOf("São Paulo", "Rio de Janeiro", "Bahia"),
                            modifier = Modifier.weight(1f)
                        )
                        DropdownMenuField(
                            value = viewModel.selectedCity.value,
                            label = AppStrings.CIDADE,
                            onValueChange = { selectedCity -> viewModel.updateCity(selectedCity) },
                            options = listOf("Campinas", "Niterói", "Salvador"),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Lista de Serviços
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(serviceList) { service ->
                            ServiceCard(
                                id = service.id,
                                name = service.name,
                                description = service.description,
                                image = null,
                                value = service.value,
                                onClick = { navController.navigate("service/${service.id}") }
                            )
                        }
                    }
                }
            }
        }
    }
}
