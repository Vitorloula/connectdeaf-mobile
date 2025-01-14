package com.connectdeaf.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.connectdeaf.ui.screens.FAQScreen
import com.connectdeaf.ui.screens.HomeScreen
import com.connectdeaf.ui.screens.ProfileScreen
import com.connectdeaf.ui.screens.RegisterInitialScreen
import com.connectdeaf.ui.screens.RegisterProfessionalScreen
import com.connectdeaf.ui.screens.RegisterScreen
import com.connectdeaf.ui.screens.ServicesScreen
import com.connectdeaf.ui.screens.SignInScreen


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "registerInitialScreen"
    ) {
        composable("loginScreen") { SignInScreen( navController = navController ) }
        composable("registerInitialScreen") { RegisterInitialScreen(navController = navController) }
        composable("registerProfessionalScreen") { RegisterProfessionalScreen(navController = navController, onClick = {}) }
        composable("registerScreen") { RegisterScreen(navController = navController, onClick = {}) }
        composable("home") { HomeScreen(navController = navController) }
        composable("profile") { ProfileScreen(navController = navController) }
        composable("register") { RegisterScreen(navController = navController, onClick = {}) }
        composable("services") { ServicesScreen(navController = navController) }
        composable("faq") { FAQScreen(navController = navController) }
    }
}
