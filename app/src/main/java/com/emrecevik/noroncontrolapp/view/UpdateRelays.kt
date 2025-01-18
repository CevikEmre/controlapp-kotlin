package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateRelays(deviceId: Long) {
    val deviceVM: DeviceViewModel = viewModel()
    val relays = deviceVM.relays.collectAsState()
    val isLoading = deviceVM.relayLoading.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    val updatedRelays = remember { mutableStateListOf<String?>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // İlk yükleme
    LaunchedEffect(Unit) {
        deviceVM.getRelays(deviceId = deviceId)
    }

    // Gelen rölelerin güncellenmesi
    LaunchedEffect(relays.value) {
        updatedRelays.clear()
        relays.value?.let { updatedRelays.addAll(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Röle Güncelleme") },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Düzenle",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            deviceVM.updateRelays(deviceId, updatedRelays.filterNotNull()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Röleler başarıyla güncellendi.")
                                }
                                isEditing = false
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Send,
                                contentDescription = "Kaydet",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                )
            }
        },
        content = { padding ->
            if (isLoading.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (relays.value.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Röle bilgisi bulunamadı.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else {
                        relays.value!!.forEachIndexed { index, relayName ->
                            if (isEditing) {
                                TextField(
                                    value = updatedRelays[index] ?: "",
                                    onValueChange = { updatedRelays[index] = it },
                                    label = { Text("Röle ${index + 1}") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                RelayCard(relayName = relayName ?: "Bilinmeyen Röle", index = index)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun RelayCard(relayName: String, index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Röle ${index + 1}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = relayName,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


