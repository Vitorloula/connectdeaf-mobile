package com.connectdeaf.ui.screens.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.connectdeaf.R
import com.connectdeaf.chat.AppData
import com.connectdeaf.chat.DataRepository
import com.connectdeaf.ui.components.chat.CardConversa
import com.connectdeaf.domain.model.chat.Conversa
import com.connectdeaf.domain.model.chat.FirebaseUser
import com.connectdeaf.ui.components.DrawerMenu
import com.connectdeaf.ui.components.TopAppBar
import com.connectdeaf.viewmodel.DrawerViewModel
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

private fun irParaConversa(navController: NavController, idConversa: String?) {
    if (idConversa == null) {
        return;
    }
    navController.navigate("chat/${idConversa}")
}

private fun entrarNaConversa(context: Context, navController: NavController, usuario1: FirebaseUser?, usuario2: FirebaseUser) {
    if (usuario1 == null) {
        Toast.makeText(context, "Desculpe, você precisa estar logado para entrar em uma conversa.", Toast.LENGTH_SHORT).show()
        return
    }

    val appData = AppData.getInstance()
    val db = FirebaseFirestore.getInstance()

    var conversa = appData.getConversaByUsers(usuario1.uid, usuario2.uid)

    if (conversa == null) {
        conversa = Conversa(
            id = null,
            mensagens = listOf(),
            usuario1 = usuario1,
            usuario2 = usuario2
        )

        // Converte a conversa para um Map antes de salvar
        val conversaMap = conversa.toMap()

        // Salva a conversa no Firestore
        db.collection("chatRooms").add(conversaMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Obtém o ID gerado pelo Firestore
                val documentId = task.result?.id

                // Atualiza o documento no Firestore para adicionar o campo "id"
                if (documentId != null) {
                    db.collection("chatRooms").document(documentId)
                        .update("id", documentId)
                        .addOnSuccessListener {
                            // Atualiza o ID da conversa localmente
                            conversa.id = documentId

                            // Adiciona a conversa à lista de conversas no AppData
                            appData.addConversa(conversa)

                            // Navega para a tela de chat
                            irParaConversa(navController, conversa.id)

                            Toast.makeText(context, "Conversa criada com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context, "Erro ao atualizar o ID da conversa", Toast.LENGTH_SHORT).show()
                            Log.e("ChatListScreen", "Erro ao atualizar o ID da conversa", exception)
                        }
                }
            } else {
                Toast.makeText(context, "Desculpe, ocorreu um erro ao criar a conversa", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        irParaConversa(navController, conversa.id)
    }
}

@Composable
fun ChatListScreen(
    navController: NavController,
    drawerViewModel: DrawerViewModel = viewModel()
) {
    var text by remember { mutableStateOf("") } // Estado para o texto de filtro
    var users by remember { mutableStateOf(listOf<FirebaseUser>()) } // Lista de usuários
    var conversas by remember { mutableStateOf(listOf<Conversa>()) } // Lista de conversas
    val db = FirebaseFirestore.getInstance()
    val appData = AppData.getInstance()
    val context = LocalContext.current
    val dataRepository = DataRepository(context)
    val scope = rememberCoroutineScope()

    // Coletar os valores do DataRepository
    var userId by remember { mutableStateOf<String?>(null) }
    var userEmail by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf<String?>(null) }

    // Listas filtradas
    val filteredUsers = users.filter { user ->
        user.name?.contains(text, ignoreCase = true) == true
    }

    val filteredConversas = conversas.filter { conversa ->
        conversa.usuario1?.name?.contains(text, ignoreCase = true) == true ||
                conversa.usuario2?.name?.contains(text, ignoreCase = true) == true
    }

    LaunchedEffect(Unit) {
        userId = dataRepository.getCurrentUserId()
        userEmail = dataRepository.getCurrentUserEmail()
        userName = dataRepository.getCurrentUserName()

        // Recuperando os usuários
        if (userId != null) {
            db.collection("users").whereNotEqualTo("uid", userId).get()
                .addOnSuccessListener { documents ->
                    val usuarios = documents.map { document ->
                        document.toObject(FirebaseUser::class.java)
                    }
                    Log.e("ChatListScreen", "Usuários recuperados: $usuarios")
                    users = usuarios.toList()
                    Log.e("ChatListScreen", "Users: $users")
                }
                .addOnFailureListener { exception ->
                    Log.e("ChatListScreen", "Erro ao recuperar usuários", exception)
                }

            // Recuperando as conversas
            db.collection("chatRooms")
                .where(
                    Filter.or(
                        Filter.equalTo("usuario1.uid", userId),
                        Filter.equalTo("usuario2.uid", userId)
                    )
                )
                .get()
                .addOnSuccessListener { documents ->
                    val localConversas = documents.map { document ->
                        val conversa = document.toObject(Conversa::class.java)
                        conversa.id = document.id
                        conversa
                    }
                    conversas = localConversas
                    appData.setConversas(localConversas)
                }
                .addOnFailureListener { exception ->
                    Log.e("ChatListScreen", "Erro ao recuperar conversas", exception)
                }
        }
    }

    val onClickAmigo = { usuario: FirebaseUser ->
        val usuarioLogado = FirebaseUser(userId, userEmail, userName)
        entrarNaConversa(context, navController, usuarioLogado, usuario)
    }

    val onClickConversa = { conversa: Conversa -> irParaConversa(navController, conversa.id) }

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
                    navController = navController
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
                // Campo de busca
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Procure pessoas ou chats...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                    modifier = Modifier.fillMaxWidth(),
                )

                // Lista de usuários filtrados
                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp, top = 24.dp),
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (filteredUsers.isEmpty()) {
                            item {
                                Text(text = "Nenhum usuário encontrado", style = MaterialTheme.typography.bodyMedium)
                            }
                        } else {
                            filteredUsers.forEach { user ->
                                item {
                                    AvatarChat(user, onClickAmigo)
                                }
                            }
                        }
                    }
                }

                // Lista de conversas filtradas
                Text(
                    text = "Mensagens",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (filteredConversas.isEmpty()) {
                        item {
                            Text(
                                text = "Nenhuma conversa encontrada",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        filteredConversas.forEach { conversa ->
                            item {
                                CardConversa(conversa, onClickConversa)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AvatarChat(user: FirebaseUser, onClick: (FirebaseUser) -> Unit = {}) {
    val userName = user.name ?: "Usuário Desconhecido"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.width(48.dp).clickable { onClick(user) }
    ) {
        Box(
            modifier = Modifier.height(48.dp).width(48.dp),
        ) {
            AsyncImage(
                model = R.drawable.avatar,
                contentDescription = "Avatar do usuário",
                modifier = Modifier.height(48.dp).width(48.dp),
            )
        }
        Text(text = userName, style = MaterialTheme.typography.labelSmall, maxLines = 1)
    }
}
