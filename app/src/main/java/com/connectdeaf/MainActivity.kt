package com.connectdeaf

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.connectdeaf.data.repository.AuthRepository
import com.connectdeaf.domain.model.chat.FcmToken
import com.connectdeaf.navigation.AppNavigation
import com.connectdeaf.ui.screens.RegisterScreen
import com.connectdeaf.ui.theme.ConnectDeafTheme
import com.connectdeaf.utils.initializeNotifications
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeNotifications(this)

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        val TAG = "token-teste"

        val context = applicationContext

        var db = FirebaseFirestore.getInstance()
        var authRepository = AuthRepository(context)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            GlobalScope.launch {
                val userId = authRepository.getUserId()
                val fcmToken = FcmToken(token)

                if (userId == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Desculpe, ocorreu um erro ao setar o token", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                db.collection("fcmTokens").document(userId).set(fcmToken).addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        // Token set successfully
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "Desculpe, ocorreu um erro ao setar o token", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            Log.d(TAG, "token de teste: $token")
//            Toast.makeText(baseContext, "token de teste: $token", Toast.LENGTH_SHORT).show()
        })


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




