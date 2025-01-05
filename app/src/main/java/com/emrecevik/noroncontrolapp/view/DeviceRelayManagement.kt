package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.emrecevik.noroncontrolapp.R
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.emrecevik.noroncontrolapp.model.requestBody.SetRelay
import com.emrecevik.noroncontrolapp.viewmodel.RelayViewModel
@Composable
fun DeviceRelayManagementTab(deviceId: Long) {
    val relayVM: RelayViewModel = viewModel()
    var timeValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by relayVM.isLoading.collectAsState()
    val relayResponse by relayVM.relayResponse.collectAsState()
    val errorMessageState by relayVM.errorMessage.collectAsState()
    var isCheckboxChecked by remember { mutableStateOf(false) }

    val successAnimation = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    val errorAnimation = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    var showAnimation by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cihaz Yönetimi",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Checkbox(
                        checked = isCheckboxChecked,
                        onCheckedChange = { isChecked ->
                            isCheckboxChecked = isChecked
                            if (!isChecked) {
                                timeValue = "" // Checkbox kapatıldığında TextField sıfırlanır
                                errorMessage = null
                            }
                        }
                    )
                    Text(
                        text = "Süreli Komut Gönder",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                if (isCheckboxChecked) {
                    OutlinedTextField(
                        value = timeValue,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                timeValue = newValue
                                errorMessage = null
                            } else {
                                errorMessage = "Sadece rakam girebilirsiniz!"
                            }
                        },
                        label = { Text("Süreyi Girin (ms) (Varsayılan: 0)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        isError = errorMessage != null,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Button(
                    onClick = {
                        val setRelay = SetRelay(
                            setrelay = "1",
                            time = if (timeValue.isEmpty()) "0" else timeValue,
                            type = "1",
                            deviceId = deviceId.toInt()
                        )
                        relayVM.setRelay(setRelay)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Gönder",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Komutu Gönder")
                }

                // Yükleme Durumu
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
                }
            }
        }

        if (showAnimation) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    .align(Alignment.Center)
            ) {
                val composition = if (isSuccess) successAnimation else errorAnimation
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        composition = composition.value,
                        iterations = 1,
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = if (isSuccess) "Komut başarıyla gönderildi." else "Komut gönderilemedi.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        LaunchedEffect(relayResponse, errorMessageState) {
            if (relayResponse != null) {
                isSuccess = true
                showAnimation = true
            } else if (errorMessageState != null) {
                isSuccess = false
                showAnimation = true
            }
            if (showAnimation) {
                kotlinx.coroutines.delay(3000) // 3 saniye
                showAnimation = false
            }
        }
    }
}

