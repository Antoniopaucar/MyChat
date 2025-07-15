package com.example.mychat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mychat.ui.LoginScreen
import com.example.mychat.ui.theme.MyChatTheme
import androidx.room.Room
import com.example.mychat.ui.MyChatDatabase
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.example.mychat.ui.ChatScreen
import com.example.mychat.ui.RegisterScreen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            // Inicializar Room usando createFromAsset
            val db = Room.databaseBuilder(
                context,
                MyChatDatabase::class.java,
                "mychat.db"
            ).createFromAsset("mychat.db").build()
            val usuarioDao = db.usuarioDao()
            MyChatTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            val isLoggedInState = remember { mutableStateOf(false) }
                            var errorMessage by remember { mutableStateOf("") }
                            if (isLoggedInState.value) {
                                ChatScreen(
                                    onMenuOptionSelected = { /* TODO: manejar menú */ },
                                    onSendMessage = { /* TODO: manejar envío */ }
                                )
                            } else {
                                LoginScreen(
                                    onLoginClick = { _, _ -> },
                                    onRegisterClick = {
                                        navController.navigate("register")
                                    },
                                    usuarioDao = usuarioDao,
                                    errorMessage = errorMessage,
                                    setErrorMessage = { errorMessage = it },
                                    setLoggedIn = { isLoggedInState.value = it }
                                )
                            }
                        }
                        composable("register") {
                            RegisterScreen(
                                onBackToLogin = {
                                    navController.popBackStack()
                                },
                                usuarioDao = usuarioDao
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyChatTheme {
        Greeting("Android")
    }
}