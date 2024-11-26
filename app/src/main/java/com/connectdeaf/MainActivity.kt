package com.connectdeaf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.connectdeaf.ui.screens.RegisterScreen
import com.connectdeaf.ui.theme.ConnectDeafTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            ConnectDeafTheme {
                RegisterScreen {  }
            }
        }
    }
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "Register" // Tela initial é definite como "Register"
    ) {
        composable("Register") {
        }
    }
}
@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onClick = {})
}




