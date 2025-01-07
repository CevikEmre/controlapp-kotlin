package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emrecevik.noroncontrolapp.NoroncontrolappApplication
import com.emrecevik.noroncontrolapp.components.ProfileCard
import com.emrecevik.noroncontrolapp.session.SessionManager
import com.emrecevik.noroncontrolapp.viewmodel.ClientViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val clientVM: ClientViewModel = viewModel()
    val sessionManager = SessionManager(context = NoroncontrolappApplication.Companion.appContext())
    val token = sessionManager.getAccessToken()
    val clientDetails = clientVM.clientDetails.collectAsState()
    val loading = clientVM.loading.collectAsState()

    LaunchedEffect(Unit) {
        clientVM.getClientDetails(token)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (loading.value) {
            // Yükleme Ekranı
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }
        } else {
            // Profil Ekranı
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Kullanıcı Adı
                Text(
                    text = clientDetails.value?.name ?: "N/A",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Profil Bilgileri
                ProfileCard("Email", clientDetails.value?.email ?: "N/A")
                ProfileCard("Telefon", clientDetails.value?.phone ?: "N/A")
                ProfileCard("Adres", clientDetails.value?.address ?: "N/A")
                ProfileCard("Şehir", clientDetails.value?.city ?: "N/A")
                ProfileCard("Ülke", clientDetails.value?.country ?: "N/A")

                // Aktif Durum
                Text(
                    text = if (clientDetails.value?.enable == true) "Aktif" else "Pasif",
                    color = if (clientDetails.value?.enable == true)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
