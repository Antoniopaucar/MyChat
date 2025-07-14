package com.example.mychat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var selectedIndex by remember { mutableStateOf(-1) }
    var usuarios by remember { mutableStateOf(listOf<Usuario>()) }

    fun limpiarCampos() {
        nombre = ""
        usuario = ""
        clave = ""
        correo = ""
        selectedIndex = -1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Clave") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                if (nombre.isNotBlank() && usuario.isNotBlank() && clave.isNotBlank() && correo.isNotBlank()) {
                    if (selectedIndex == -1) {
                        // Insertar
                        val nuevo = Usuario(
                            id = (usuarios.maxOfOrNull { it.id } ?: 0) + 1,
                            nombre = nombre,
                            usuario = usuario,
                            clave = clave,
                            correo = correo
                        )
                        usuarios = usuarios + nuevo
                        limpiarCampos()
                    } else {
                        // Actualizar
                        usuarios = usuarios.mapIndexed { idx, u ->
                            if (idx == selectedIndex) u.copy(nombre = nombre, usuario = usuario, clave = clave, correo = correo) else u
                        }
                        limpiarCampos()
                    }
                }
            }) {
                Text(if (selectedIndex == -1) "Insertar" else "Actualizar")
            }
            Button(onClick = {
                if (selectedIndex != -1) {
                    usuarios = usuarios.filterIndexed { idx, _ -> idx != selectedIndex }
                    limpiarCampos()
                }
            }, enabled = selectedIndex != -1) {
                Text("Eliminar")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Usuarios registrados:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(usuarios) { usuarioItem ->
                val idx = usuarios.indexOf(usuarioItem)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .then(Modifier),
                    onClick = {
                        selectedIndex = idx
                        nombre = usuarioItem.nombre
                        usuario = usuarioItem.usuario
                        clave = usuarioItem.clave
                        correo = usuarioItem.correo
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${usuarioItem.id}. ${usuarioItem.nombre} (${usuarioItem.usuario}) - ${usuarioItem.correo}")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onBackToLogin) {
            Text("Volver al Login")
        }
    }
} 