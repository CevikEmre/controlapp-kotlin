package com.emrecevik.noroncontrolapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.emrecevik.noroncontrolapp.view.DeviceDetailScreen
import com.emrecevik.noroncontrolapp.view.LoginScreen
import com.emrecevik.noroncontrolapp.view.MainScreen
import com.emrecevik.noroncontrolapp.view.RegisterScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    NavControllerHolder.navController = navController

    NavHost(navController = navController, startDestination = Screen.Login.screen) {
        // Login Screen
        composable(Screen.Login.screen) {
            LoginScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }
        // Register Screen
        composable(Screen.Register.screen) {
            RegisterScreen(navController = navController)
        }
        // Main Screen
        composable(Screen.Main.screen) {
            MainScreen(navController)
        }
        // Device Details Screen
        composable(
            route = "${Screen.DeviceDetails.screen}/{deviceId}",
            arguments = listOf(navArgument("deviceId") { type = NavType.LongType })
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getLong("deviceId") ?: 0L
            DeviceDetailScreen(navController = navController, deviceId = deviceId)
        }
    }
}
