package com.connectdeaf.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.connectdeaf.data.repository.AuthRepository
import com.connectdeaf.data.repository.ServicesRepository
import com.connectdeaf.ui.components.DropdownMenuField
import com.connectdeaf.ui.components.GenericInputField
import com.connectdeaf.ui.components.HeaderSectionRegister
import com.connectdeaf.ui.components.TopAppBar
import com.connectdeaf.ui.theme.AppStrings
import com.connectdeaf.ui.theme.PrimaryColor
import com.connectdeaf.viewmodel.ServiceProfessionalViewModel
import com.connectdeaf.viewmodel.ServicesViewModel
import com.connectdeaf.viewmodel.factory.ServicesByProfessionalViewModelFactory
import com.connectdeaf.viewmodel.factory.ServicesViewModelFactory
import com.connectdeaf.viewmodel.uistate.ServicesProfessionalUiState

@Composable
fun RegisterServiceScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val servicesRepository = ServicesRepository(context.applicationContext)
    val servicesProfessionalViewModel: ServiceProfessionalViewModel = viewModel(
        factory = ServicesByProfessionalViewModelFactory(
            context = context.applicationContext,
            servicesRepository = servicesRepository
        )
    )

    val uiState by servicesProfessionalViewModel.uiState.collectAsState()
    val idProfessional = AuthRepository(context).getProfessionalId() ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                showBackButton = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSectionRegister(isService = true)

            Spacer(modifier = Modifier.height(8.dp))

            RegisterInputFields(
                uiState = uiState,
                onNameServiceChange = servicesProfessionalViewModel::onNameServiceChange,
                onDescriptionChange = servicesProfessionalViewModel::onDescriptionChange,
                onCategoryChange = servicesProfessionalViewModel::onCategoryChange,
                onPriceChange = servicesProfessionalViewModel::onPriceChange,
                onStateChange = servicesProfessionalViewModel::onStateChange,
                onCityChange = servicesProfessionalViewModel::onCityChange
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Seção de upload de imagem
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(311.dp, 116.dp)
                    .drawDashedBorder(PrimaryColor)
                    .clickable {
                        selectImage(context) { uri ->
                            servicesProfessionalViewModel.onImageSelected(uri)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.imageUri == null) {
                    ImagePlaceholder()
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.imageUri),
                        contentDescription = "Imagem Selecionada",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botão de salvar
            OutlinedButton(
                onClick = {
                    if (uiState.isFormValid) {
                        servicesProfessionalViewModel.createServiceByProfessional(idProfessional, context,
                            onSuccess = {
                                Toast.makeText(context, "Serviço criado com sucesso!", Toast.LENGTH_SHORT).show()
                                navController.navigate("serviceProfessionalScreen")
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Formulário inválido. Verifique os campos.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                enabled = uiState.isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isFormValid) PrimaryColor else Color(0xFF999999),
                    contentColor = if (uiState.isFormValid) Color.White else PrimaryColor
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    "Salvar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RegisterInputFields(
    uiState: ServicesProfessionalUiState,
    onNameServiceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onStateChange: (String) -> Unit = {},
    onCityChange: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val servicesViewModel: ServicesViewModel = viewModel(
        factory = ServicesViewModelFactory(context)
    )

    LaunchedEffect(Unit) {
        servicesViewModel.loadStatesFromIBGE(context = context)
    }

    GenericInputField(
        value = uiState.nameService,
        onValueChange = onNameServiceChange,
        label = AppStrings.NAME_SERVICE,
        isError = uiState.nameServiceError != null,
        errorMessage = uiState.nameServiceError ?: ""
    )
    GenericInputField(
        value = uiState.description,
        onValueChange = onDescriptionChange,
        label = AppStrings.DESCRIPTION_SERVICE,
        isError = uiState.descriptionError != null,
        errorMessage = uiState.descriptionError ?: ""
    )
    GenericInputField(
        value = uiState.categories,
        onValueChange = onCategoryChange,
        label = AppStrings.CATEGORY_SERVICE,
        isError = uiState.categoriesError != null,
        errorMessage = uiState.categoriesError ?: ""
    )
    GenericInputField(
        value = uiState.price,
        onValueChange = onPriceChange,
        label = AppStrings.PRICE_SERVICE,
        keyboardType = KeyboardType.Number,
        isError = uiState.priceError != null,
        errorMessage = uiState.priceError ?: ""
    )
    DropdownMenuField(
        value = uiState.state,
        label = AppStrings.ESTADO,
        onValueChange = { newState ->
            onStateChange(newState)
            onCityChange("") // Limpar a cidade ao selecionar um novo estado
            servicesViewModel.loadCitiesForStateFromIBGE(newState, context) // Carregar as cidades ao selecionar o estado
        },
        options = servicesViewModel.states,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    DropdownMenuField(
        value = uiState.city,
        label = AppStrings.CIDADE,
        onValueChange = { onCityChange(it) },
        options = servicesViewModel.cities,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun ImagePlaceholder() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Upload",
            tint = PrimaryColor,
            modifier = Modifier.size(40.dp)
        )
        Text(text = "PNG ou JPG (max. 3MB)", color = PrimaryColor, fontSize = 12.sp)
    }
}

fun Modifier.drawDashedBorder(color: Color): Modifier {
    return this.drawBehind {
        val dashWidth = 6.dp.toPx()
        val dashGap = 4.dp.toPx()
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = 2.dp.toPx()
            pathEffect = android.graphics.DashPathEffect(floatArrayOf(dashWidth, dashGap), 0f)
        }

        val path = android.graphics.Path().apply {
            addRoundRect(
                0f, 0f, size.width, size.height,
                8.dp.toPx(), 8.dp.toPx(),
                android.graphics.Path.Direction.CW
            )
        }

        drawContext.canvas.nativeCanvas.drawPath(path, paint)
    }
}

fun selectImage(context: Context, onImageSelected: (Uri) -> Unit) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    if (context is Activity) {
        context.startActivityForResult(intent, 100)
    }
}

@Preview
@Composable
fun RegisterServiceScreenPreview() {
    RegisterServiceScreen( navController = NavController(LocalContext.current))
}
