package com.emrecevik.noroncontrolapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.emrecevik.noroncontrolapp.navigation.NavigationHost
import com.emrecevik.noroncontrolapp.ui.theme.NoroncontrolappTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        } else {
            getFCMToken()
        }

        setContent {
            NoroncontrolappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavigationHost()
                }
            }
        }
    }

    /**
     * Android 13 (API 33) ve üzeri cihazlarda bildirim izni kontrol eder.
     */
    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getFCMToken()
        } else {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    /**
     * Bildirim izni istemek için Activity Result Launcher
     */
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d("Notification", "Bildirim izni verildi.")
                getFCMToken()
            } else {
                Log.e("Notification", "Bildirim izni reddedildi.")
            }
        }


    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM Token", "Device Token: $token")
                    NoroncontrolappApplication.deviceToken = token
                } else {
                    Log.e("FCM Token", "Token alma başarısız", task.exception)
                }
            }
    }
}
