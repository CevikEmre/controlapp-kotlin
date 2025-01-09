package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emrecevik.noroncontrolapp.model.response.Devices
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonsProcess(device: Devices) {
    val deviceVM: DeviceViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val tfValue = remember { mutableStateOf("") }

    val isSuccess = deviceVM.addSuccess.collectAsState()
    val isError = deviceVM.addError.collectAsState()
    val isRefreshing = deviceVM.isRefreshing
    val errorMessage = deviceVM.addUserErrorMessage.collectAsState()

    val users = deviceVM.users.collectAsState()
    val expandedStates = remember { mutableStateOf(emptyList<Boolean>()) }

    // Senkronize expandedStates
    LaunchedEffect(users.value) {
        expandedStates.value = users.value?.map { false } ?: emptyList()
    }

    LaunchedEffect(Unit) {
        deviceVM.getUsersForDevice(device.devId)
    }

    if (isRefreshing.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Telefon numarası girişi
                    OutlinedTextField(
                        value = tfValue.value,
                        onValueChange = {
                            if (it.isDigitsOnly()) {
                                tfValue.value = it
                            }
                        },
                        modifier = Modifier.padding(6.dp),
                        label = { Text("Telefon Numarası") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("Örnek: 905551112233") }
                    )

                    if (isError.value && errorMessage.value != null) {
                        Text(
                            text = errorMessage.value ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    // Kullanıcı ekleme butonu
                    Button(
                        onClick = {
                            scope.launch {
                                deviceVM.addUserToDevice(
                                    devId = device.devId,
                                    phone = tfValue.value
                                )
                                deviceVM.getUsersForDevice(device.devId)
                            }
                        },
                        enabled = !isRefreshing.value
                    ) {
                        Text("Ekle")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Kullanıcı listesi başlığı
                    Text(
                        text = "Bağlı Kullanıcılar",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Kullanıcı listesi
                    if (users.value.isNullOrEmpty()) {
                        Text(
                            text = "Kullanıcı bulunamadı.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            users.value?.let { userList ->
                                items(userList.size) { index ->
                                    if (index >= expandedStates.value.size) return@items

                                    val client = userList[index]
                                    val isExpanded = expandedStates.value[index]

                                    Card(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp)
                                            .clickable {
                                                expandedStates.value =
                                                    expandedStates.value.mapIndexed { i, state ->
                                                        if (i == index) !state else state
                                                    }
                                            }
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxSize(),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "Ad: ${client?.name}",
                                                style = MaterialTheme.typography.bodyLarge
                                            )

                                            if (isExpanded) {
                                                Text(
                                                    text = "Telefon: ${client?.phone}",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = "Adres: ${client?.address}, ${client?.city}, ${client?.country}",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                                Button(
                                                    onClick = {
                                                        scope.launch {
                                                            if (client != null) {
                                                                deviceVM.removeUserFromDevice(
                                                                    device.devId,
                                                                    client.phone
                                                                )
                                                            }
                                                            deviceVM.getUsersForDevice(device.devId)

                                                            // Kullanıcı silindikten sonra expandedStates'i güncelle
                                                            expandedStates.value =
                                                                deviceVM.users.value?.map { false }
                                                                    ?: emptyList()
                                                        }
                                                    },
                                                    modifier = Modifier.align(Alignment.End)
                                                ) {
                                                    Text("Sil")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isRefreshing.value) {
                        Text(
                            text = "İşlem devam ediyor...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        )
    }
}
