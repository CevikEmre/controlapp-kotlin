package com.emrecevik.noroncontrolapp

import android.util.Log
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class NoroncontrolappApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: NoroncontrolappApplication? = null

        fun appContext() = instance!!.applicationContext

        var deviceToken: String? = null
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // Sadece izin verilmişse token al
        if (checkNotificationPermission()) {
            getFCMToken()
        }
    }

    private fun checkNotificationPermission(): Boolean {
        val permissionGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!permissionGranted) {
            Log.e("Notification", "Bildirim izni verilmedi.")
        }
        return permissionGranted
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.e("FCM Token", "Device Token: $token")
                    deviceToken = token
                } else {
                    Log.e("FCM Token", "Token alma başarısız", task.exception)
                }
            }
    }
}
