package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emrecevik.noroncontrolapp.model.response.GetAllDevicesForClient
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(navController: NavController, deviceId: Long) {
    val deviceViewModel: DeviceViewModel = viewModel()
    val device by deviceViewModel.device.collectAsState()
    val isLoading by deviceViewModel.isLoading
    val errorMessage by deviceViewModel.errorMessage.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // Tab durum değişkeni

    LaunchedEffect(deviceId) {
        deviceViewModel.getDeviceDetail(deviceId.toInt())
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cihaz Detayları - $deviceId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Detaylar") },
                    label = { Text("Detaylar") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.AccountBox, contentDescription = "Bağlantılar") },
                    label = { Text("Bağlantılar") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Tarihler") },
                    label = { Text("Tarihler") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Yönetim") },
                    label = { Text("Yönetim") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Bir hata oluştu.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                device != null -> {
                    when (selectedTab) {
                        0 -> DeviceDetailTab(device!!)
                        3 -> DeviceRelayManagementTab(deviceId)
                    }
                }
                else -> {
                    Text(
                        text = "Cihaz detayları yüklenemedi.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}



@Composable
fun DeviceDetailTab(device: GetAllDevicesForClient) {
    Card(
        modifier = Modifier
            .fillMaxSize() // Kart tüm ekranı kaplasın
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Cihaz Detayları",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Kaydırılabilir Detay Listesi
            val detailItems = listOf(
                "Cihaz ID" to device.devId.toString(),
                "Cihaz Tipi" to (device.deviceType ?: "Bilinmiyor"),
                "Durum" to (if (device.connected == true) "Bağlı" else "Bağlı Değil"),
                "M2M Numarası" to (device.m2mNumber ?: "Bilinmiyor"),
                "M2M Seri Numarası" to (device.m2mSerial ?: "Bilinmiyor"),
                "Oluşturulma Tarihi" to (device.createdDateTime ?: "Bilinmiyor"),
                "Aktivasyon Tarihi" to (device.activatedDateTime ?: "Henüz Aktive Edilmedi"),
                "Aktif Gün Sayısı" to device.activeDays.toString(),
                "Yıllık Ücret" to "${device.yearlyPrice} $"
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Kartlar arasında boşluk
            ) {
                items(detailItems.size) {
                    val item = detailItems[it]
                    DetailCard(label = item.first, value = item.second)
                }
            }

        }
    }
}

@Composable
fun DetailCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
