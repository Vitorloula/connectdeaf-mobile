package com.connectdeaf

import android.app.NotificationChannel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.connectdeaf.di.appModule
import com.connectdeaf.navigation.AppNavigation
import com.connectdeaf.ui.screens.RegisterScreen
import com.connectdeaf.ui.theme.ConnectDeafTheme
import com.connectdeaf.utils.createNotificationChannel
import com.connectdeaf.utils.initializeNotifications
import com.connectdeaf.utils.requestNotificationPermission
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeNotifications(this)

        installSplashScreen()
        setContent {
            val navController = rememberNavController()

            enableEdgeToEdge()
            ConnectDeafTheme {
                AppNavigation(
                    navController = navController
                )
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = rememberNavController())
}




