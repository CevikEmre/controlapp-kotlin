package com.emrecevik.noroncontrolapp.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emrecevik.noroncontrolapp.view.tabs.DeviceDetailTab
import com.emrecevik.noroncontrolapp.view.tabs.DeviceInfoTab
import com.emrecevik.noroncontrolapp.view.tabs.DeviceRelayManagementTab
import com.emrecevik.noroncontrolapp.view.tabs.PersonsProcess
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(navController: NavController, deviceId: Long) {
    val deviceViewModel: DeviceViewModel = viewModel()
    val device by deviceViewModel.device.collectAsState()
    val isLoading by deviceViewModel.isLoading
    val errorMessage by deviceViewModel.errorMessage.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(deviceId) {
        deviceViewModel.getDeviceDetail(deviceId)
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
            device?.let { nonNullDevice ->
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
                        icon = { Icon(Icons.Default.Build, contentDescription = "Yönetim") },
                        label = { Text("Yönetim") }
                    )
                    if (nonNullDevice.isAdmin) {
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Person") },
                            label = { Text("Kişi İşlemleri") }
                        )
                    }
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(Icons.Default.Info, contentDescription = "Detaylar") },
                        label = { Text("Cihaz Durumu") }
                    )
                }
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
                        0 -> {
                            if (device != null) {
                                DeviceDetailTab(device!!)
                            } else {
                                Text(
                                    text = "Cihaz detayları yükleniyor...",
                                    modifier = Modifier.align(Alignment.Center),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                        1 -> DeviceRelayManagementTab(deviceId)
                        2 -> if (device!!.isAdmin) PersonsProcess(device!!)
                        3 -> DeviceInfoTab(deviceId)
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

