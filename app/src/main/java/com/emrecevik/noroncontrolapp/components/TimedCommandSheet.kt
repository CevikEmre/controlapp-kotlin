package com.emrecevik.noroncontrolapp.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimedCommandSheet(
    timeValue: Long,
    onTimeValueChange: (Long) -> Unit,
    errorMessage: String?,
    onSendCommand: (String, Long) -> Unit,
    deviceId: Long
) {
    val deviceVM: DeviceViewModel = viewModel()
    val relayNames = deviceVM.relays.collectAsState()
    LaunchedEffect(Unit) {
        deviceVM.getRelays(deviceId)
    }

    var selectedRelay by remember { mutableStateOf<Int?>(null) }
    var isTimedRelay by remember { mutableStateOf(false) }

    val hours = (timeValue / 3600).toInt()
    val minutes = ((timeValue % 3600) / 60).toInt()
    val seconds = (timeValue % 60).toInt()

    var hoursInput by remember { mutableStateOf(hours.toString()) }
    var minutesInput by remember { mutableStateOf(minutes.toString()) }
    var secondsInput by remember { mutableStateOf(seconds.toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (relayNames.value.isNullOrEmpty()) {
            Text(
                text = "Röle bilgisi yüklenemedi veya mevcut değil.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(relayNames.value!!.size) { index ->
                    val relayName = relayNames.value!![index] ?: "Röle ${index + 1}"
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedRelay = index + 1
                            },
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedRelay == index + 1)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = relayName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedRelay == index + 1)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedRelay != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isTimedRelay,
                        onCheckedChange = { isTimedRelay = it }
                    )
                    Text(text = "Zamanlı Röle")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Zaman Giriş Alanları
            if (isTimedRelay) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = hoursInput,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                hoursInput = newValue
                                val totalSeconds = (newValue.toIntOrNull() ?: 0) * 3600 +
                                        (minutesInput.toIntOrNull() ?: 0) * 60 +
                                        (secondsInput.toIntOrNull() ?: 0)
                                onTimeValueChange(totalSeconds.toLong())
                            }
                        },
                        label = { Text("Saat") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = minutesInput,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                minutesInput = newValue
                                val totalSeconds = (hoursInput.toIntOrNull() ?: 0) * 3600 +
                                        (newValue.toIntOrNull() ?: 0) * 60 +
                                        (secondsInput.toIntOrNull() ?: 0)
                                onTimeValueChange(totalSeconds.toLong())
                            }
                        },
                        label = { Text("Dakika") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = secondsInput,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                secondsInput = newValue
                                val totalSeconds = (hoursInput.toIntOrNull() ?: 0) * 3600 +
                                        (minutesInput.toIntOrNull() ?: 0) * 60 +
                                        (newValue.toIntOrNull() ?: 0)
                                onTimeValueChange(totalSeconds.toLong())
                            }
                        },
                        label = { Text("Saniye") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Button(
                onClick = {
                    selectedRelay?.let { relayIndex ->
                        onSendCommand(relayIndex.toString(), if (isTimedRelay) timeValue else 0L)
                    }
                },
                enabled = selectedRelay != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Komutu Gönder")
            }
        }
    }
}

