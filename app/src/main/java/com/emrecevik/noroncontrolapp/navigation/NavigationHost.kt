package com.emrecevik.noroncontrolapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.emrecevik.noroncontrolapp.view.AddDeviceScreen
import com.emrecevik.noroncontrolapp.view.DeviceDetailScreen
import com.emrecevik.noroncontrolapp.view.DeviceSelection
import com.emrecevik.noroncontrolapp.view.DeviceSettings
import com.emrecevik.noroncontrolapp.view.LoginScreen
import com.emrecevik.noroncontrolapp.view.MainScreen
import com.emrecevik.noroncontrolapp.view.ProfileScreen
import com.emrecevik.noroncontrolapp.view.RegisterScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    NavControllerHolder.navController = navController

    NavHost(navController = navController, startDestination = Screen.Login.screen) {
        composable(Screen.Login.screen) {
            LoginScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }
        composable(Screen.Register.screen) {
            RegisterScreen(navController = navController)
        }
        composable(Screen.Main.screen) {
            MainScreen(navController)
        }
        composable(
            route = "${Screen.DeviceDetails.screen}/{deviceId}",
            arguments = listOf(navArgument("deviceId") { type = NavType.LongType })
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getLong("deviceId") ?: 0L
            DeviceDetailScreen(navController = navController, deviceId = deviceId)
        }
        composable(Screen.AddDevice.screen) {
            AddDeviceScreen(navController = navController)
        }
        composable(Screen.Profile.screen) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Settings.screen) {
            DeviceSelection(navController = navController)
        }
        composable(
            route = "${Screen.DeviceSettings.screen}/{devId}",
            arguments = listOf(navArgument("devId") { type = NavType.LongType })
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getLong("devId") ?: 0L
            DeviceSettings(navController = navController, deviceId = deviceId)
        }
    }
}
