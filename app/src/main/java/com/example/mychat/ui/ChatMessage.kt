package com.example.mychat.ui
 
data class ChatMessage(
    val text: String,
    val isUser: Boolean // true si es del usuario, false si es de la IA
) 