package com.emrecevik.noroncontrolapp.view

import NavControllerHolder.Companion.navController
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emrecevik.noroncontrolapp.NoroncontrolappApplication
import com.emrecevik.noroncontrolapp.R
import com.emrecevik.noroncontrolapp.model.response.ClientDetails
import com.emrecevik.noroncontrolapp.model.response.GetAllDevicesForClient
import com.emrecevik.noroncontrolapp.navigation.Screen
import com.emrecevik.noroncontrolapp.session.SessionManager
import com.emrecevik.noroncontrolapp.viewmodel.ClientViewModel
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val deviceViewModel: DeviceViewModel = viewModel()
    val clientVM: ClientViewModel = viewModel()
    val devices by deviceViewModel.devices.collectAsState()
    val isLoading by remember { deviceViewModel.isLoading }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sessionManager = SessionManager(context = NoroncontrolappApplication.Companion.appContext())
    val token = sessionManager.getAccessToken()
    val clientDetails = clientVM.clientDetails.collectAsState()

    LaunchedEffect(Unit) {
        deviceViewModel.fetchDevices()
        clientVM.getClientDetails(token)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onLogout = {
                    navController.popBackStack()
                    scope.launch { drawerState.close() } // Logout sonrası drawer'ı kapat
                },
                clientDetails = clientDetails,
                drawerState = drawerState
            )
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .clickable( // Drawer dışında bir yere tıklanınca drawer'ı kapat
                    enabled = drawerState.isOpen,
                    onClick = {
                        scope.launch { drawerState.close() }
                    }
                ),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.AddDevice.screen)
                        scope.launch { drawerState.close() } // Navigate sonrası drawer'ı kapat
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Device")
                }
            },
            topBar = {
                TopAppBar(
                    title = { Text("Ana Ekran") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                    }
                )
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

                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }

                    devices.isNullOrEmpty() -> {
                        Text("Yüklenecek cihaz yok.")
                    }

                    else -> {
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
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    onLogout: () -> Unit,
    clientDetails: State<ClientDetails?>
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 8.dp)
            )
            Text(
                text = clientDetails.value?.name ?: "N/A",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = clientDetails.value?.email ?: "N/A",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menü Seçenekleri
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(menuItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            item.onClick()
                            scope.launch { drawerState.close() } // Menü seçeneğine tıklanınca drawer'ı kapat
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onLogout() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Çıkış Yap")
        }
    }
}


// Menü Elemanları İçin Veri Sınıfı
data class MenuItem(val title: String, val icon: ImageVector, val onClick: () -> Unit)

// Menü Elemanları Listesi
val menuItems = listOf(
    MenuItem(
        title = "Profil",
        icon = Icons.Default.Person,
        onClick = {
            navController.navigate(Screen.Profile.screen)

        }
    ),
    MenuItem(
        title = "Ayarlar",
        icon = Icons.Default.Settings,
        onClick = { /* Ayarlar sayfasına git */ }
    ),

    )


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
            Text(
                text = "Cihaz Tipi: ${device.deviceType}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Aktif Durum: ${if (device.connected) "Bağlı" else "Bağlı Değil"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


