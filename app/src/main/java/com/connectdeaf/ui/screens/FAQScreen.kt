package com.connectdeaf.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.connectdeaf.ui.components.DrawerMenu
import com.connectdeaf.ui.components.MessageCard
import com.connectdeaf.ui.components.TopAppBar
import com.connectdeaf.viewmodel.DrawerViewModel
import com.connectdeaf.viewmodel.FAQViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun FAQScreen(
    navController: NavController,
    faqViewModel: FAQViewModel = getViewModel(), // Injetada via Koin
    drawerViewModel: DrawerViewModel = viewModel()
) {
    var userMessage by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()

    val messageList by faqViewModel.messages.collectAsState()
    val isLoading by faqViewModel.isLoading.collectAsState()

    val listState: LazyListState = rememberLazyListState()

    DrawerMenu(
        navController = navController,
        scope = scope,
        gesturesEnabled = false,
        drawerViewModel = drawerViewModel
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    onOpenDrawerMenu = { scope.launch { drawerViewModel.openMenuDrawer() } },
                    onOpenDrawerNotifications = { scope.launch { drawerViewModel.openNotificationsDrawer() } },
                    showBackButton = true,
                    isBot = true,
                    navController = navController
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    state = listState,
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(messageList) { chatMessage ->
                        MessageCard(
                            sender = chatMessage.sender,
                            message = chatMessage.text,
                            isBot = chatMessage.isBot
                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Espaçamento entre mensagens
                    }
                }

                // Campo de digitação do usuário + botão de enviar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF3D66CC))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = userMessage,
                            onValueChange = { userMessage = it },
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White, shape = MaterialTheme.shapes.medium)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            decorationBox = { innerTextField ->
                                if (userMessage.text.isEmpty()) {
                                    Text(
                                        text = "Pergunte algo ao CDBot...",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (userMessage.text.isNotBlank()) {
                                    faqViewModel.sendMessage(userMessage.text)
                                    userMessage = TextFieldValue("")

                                    // Rola para a última mensagem após enviar
                                    scope.launch {
                                        // Aguarda a recomposição para garantir que a mensagem foi adicionada
                                        // Pode ajustar o delay conforme necessário
                                        kotlinx.coroutines.delay(100)
                                        listState.animateScrollToItem(messageList.size)
                                    }
                                }
                            },
                            enabled = !isLoading
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF9919))
                                    .padding(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Enviar mensagem",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .rotate(-45f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
