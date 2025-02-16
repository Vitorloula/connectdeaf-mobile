package com.connectdeaf.ui.screens.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.connectdeaf.chat.AppData
import com.connectdeaf.chat.DataRepository
import com.connectdeaf.domain.model.chat.Conversa
import com.connectdeaf.domain.model.chat.FirebaseUser
import com.connectdeaf.domain.model.chat.Message
import com.connectdeaf.domain.model.chat.Notificacao
import com.connectdeaf.ui.components.DrawerMenu
import com.connectdeaf.ui.components.chat.CardMensagem
import com.connectdeaf.viewmodel.DrawerViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

private fun enviarMensagem(context: Context, corpo: String, id: String?, usuarioNotificacao: String?, usuarioEnvio: FirebaseUser?, callback: () -> Unit) {
    if (id == null || usuarioEnvio == null || usuarioNotificacao == null) {
        Toast.makeText(context, "ID e/ou Usuário nulos!", Toast.LENGTH_SHORT).show()
        return;
    }

    var db = FirebaseFirestore.getInstance()

    val mensagem = Message(corpo, usuarioEnvio, Timestamp.now())

    db.collection("chatRooms").document(id).update("mensagens", FieldValue.arrayUnion(mensagem)).addOnCompleteListener { it ->
        if (it.isSuccessful) {
//            Toast.makeText(context, "Mensagem enviada com sucesso!", Toast.LENGTH_SHORT).show()
            val notificacao = Notificacao("", usuarioEnvio.name, corpo, listOf(usuarioNotificacao), usuarioEnvio.uid.toString())
            db.collection("notificacoes").add(notificacao)
            callback()
        } else {
            Toast.makeText(context, "Desculpe, ocorreu um erro ao enviar a mensagem", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun enviadoPorMim(mensagem: Message, id: String?): Boolean {
    if (id == null) {
        return false
    }

    return id == mensagem.usuario!!.uid.toString()
}

@Composable
fun ChatScreen(
    navController: NavController,
    drawerViewModel: DrawerViewModel = viewModel(),
    id: String
) {
    var text by remember { mutableStateOf("") }
    var mensagens by remember { mutableStateOf(listOf<Message>()) }
    val appData = AppData.getInstance()
    var conversa = appData.getConversaById(id)
    var name: String = if (conversa!!.usuario2!!.name != null) conversa.usuario2!!.name.toString() else ""
    val context = LocalContext.current
    var db = FirebaseFirestore.getInstance()

    val dataRepository = DataRepository(context)

    // Coletar os valores do DataRepository
    var userId by remember { mutableStateOf<String?>(null) }
    var userEmail by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        userId = dataRepository.getCurrentUserId()
        userEmail = dataRepository.getCurrentUserEmail()
        userName = dataRepository.getCurrentUserName()

        if (conversa.mensagens != null) {
            mensagens = conversa.mensagens!!
        }
    }

    val TAG = "teste-realtime"
    val docRef = db.collection("chatRooms").document(id)
    docRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()) {
            Log.d(TAG, "Current data: ${snapshot.data}")
            val localConversa = snapshot.toObject(Conversa::class.java)
            if (localConversa?.mensagens != null) {
                mensagens = localConversa.mensagens!!
            }


        } else {
            Log.d(TAG, "Current data: null")
        }
    }

    val onSuccessEnviarMensagem = { -> text = "" }
    val callEnviarMensagem = callEnviarMensagem@{ ->
        if (userEmail == null || userName == null) {
            return@callEnviarMensagem
        }
        val usuario = FirebaseUser(userId,userName, userEmail)
        val usuarioNotificacao = if (conversa.usuario1!!.uid == userId) conversa.usuario2!!.uid else conversa.usuario1!!.uid
        enviarMensagem(context, text, conversa.id, usuarioNotificacao, usuario, onSuccessEnviarMensagem)
    }

    DrawerMenu (
        navController = navController,
        scope = scope,
        gesturesEnabled = false,
        drawerViewModel = drawerViewModel
    ) {
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp).weight(1f)
                ) {
                    if (mensagens.isEmpty()) {
                        item {
                            Text(
                                text = "Ainda não há mensagens",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        mensagens.forEach { mensagem ->
                            item {
                                Column(
                                    horizontalAlignment = if (enviadoPorMim(
                                            mensagem,
                                            userId
                                        )
                                    ) Alignment.End else Alignment.Start,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    CardMensagem(mensagem, enviadoPorMim(mensagem, userId))
                                }
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = { Text("Escreva uma mensagem...") },
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(
                        onClick = { callEnviarMensagem() }
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "Enviar")
                    }
                }
            }
        }
    }
}


