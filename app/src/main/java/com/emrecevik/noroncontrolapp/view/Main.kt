package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emrecevik.noroncontrolapp.model.response.GetAllDevicesForClient
import com.emrecevik.noroncontrolapp.navigation.Screen
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel


@Composable
fun MainScreen(navController: NavController) {
    val deviceViewModel: DeviceViewModel = viewModel()
    val devices by deviceViewModel.devices.collectAsState()
    val isLoading by remember { deviceViewModel.isLoading }

    LaunchedEffect(Unit) {
        deviceViewModel.fetchDevices()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddDevice.screen) },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Device")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Ana Ekrana Hoş Geldiniz!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else if (devices.isNullOrEmpty()) {
                Text("Yüklenecek cihaz yok.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(devices!!) { device ->
                        if (device != null) {
                            DeviceItem(device, navController)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Çıkış Yap")
            }
        }
    }
}


@Composable
fun DeviceItem(device: GetAllDevicesForClient, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("${Screen.DeviceDetails.screen}/${device.devId}")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Cihaz ID: ${device.devId}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Cihaz Tipi: ${device.deviceType}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Aktif Durum: ${if (device.connected) "Bağlı" else "Bağlı Değil"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}


