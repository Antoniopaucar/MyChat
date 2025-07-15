package com.example.mychat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.mychat.ui.ChatMessage
import kotlinx.coroutines.launch
import com.example.mychat.ui.GeminiApiService
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onMenuOptionSelected: (String) -> Unit = {},
    onSendMessage: (String) -> Unit = {}
) {
    var message by remember { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    fun sendMessage() {
        val userMsg = message.trim()
        if (userMsg.isNotBlank()) {
            messages = messages + ChatMessage(userMsg, true)
            message = ""
            isLoading = true
            coroutineScope.launch {
                try {
                    val iaResponse = GeminiApiService.getChatResponse(userMsg)
                    messages = messages + ChatMessage(iaResponse, false)
                } catch (e: Exception) {
                    messages = messages + ChatMessage("Error al contactar la IA: ${e.localizedMessage}", false)
                }
                isLoading = false
                // Scroll automático al último mensaje (abajo)
                delay(100)
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat IA", modifier = Modifier.padding(top = 12.dp)) },
                modifier = Modifier.padding(top = 12.dp),
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Historial") },
                            onClick = {
                                menuExpanded = false
                                onMenuOptionSelected("historial")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mis datos") },
                            onClick = {
                                menuExpanded = false
                                onMenuOptionSelected("mis_datos")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                menuExpanded = false
                                onMenuOptionSelected("cerrar_sesion")
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe tu mensaje...") },
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (message.isNotBlank() && !isLoading) {
                            sendMessage()
                        }
                    },
                    enabled = !isLoading
                ) {
                    Text("Enviar")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                reverseLayout = false // Mensajes de arriba hacia abajo
            ) {
                items(messages) { msg ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            color = if (msg.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = msg.text,
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onPrimary.takeIf { msg.isUser } ?: MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(
                                    text = "La IA está escribiendo...",
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 