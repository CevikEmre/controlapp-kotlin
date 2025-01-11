package com.emrecevik.noroncontrolapp.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimedCommandSheet(
    timeValue: Long,
    onTimeValueChange: (Long) -> Unit,
    errorMessage: String?,
    onSendCommand: () -> Unit,
) {
    // Süre bileşenlerini hesaplamak için yardımcı fonksiyonlar
    val hours = (timeValue / 3600000).toInt()
    val minutes = ((timeValue % 3600000) / 60000).toInt()
    val seconds = ((timeValue % 60000) / 1000).toInt()
    val milliseconds = (timeValue % 1000).toInt()

    // Kullanıcı girişleri için durum
    var hoursInput by remember { mutableStateOf(hours.toString()) }
    var minutesInput by remember { mutableStateOf(minutes.toString()) }
    var secondsInput by remember { mutableStateOf(seconds.toString()) }
    var millisecondsInput by remember { mutableStateOf(milliseconds.toString()) }

    // LocalTime kullanarak mevcut saati alıyoruz
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    // Kullanıcı girişlerine göre saati güncelleyen yardımcı fonksiyon
    fun updateTime() {
        currentTime = LocalTime.now()
            .plusHours(hoursInput.toLongOrNull() ?: 0)
            .plusMinutes(minutesInput.toLongOrNull() ?: 0)
            .plusSeconds(secondsInput.toLongOrNull() ?: 0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dijital saat görüntüsü
        Text(
            text = currentTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Saat
            OutlinedTextField(
                value = hoursInput,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        hoursInput = newValue
                        val totalMillis = (newValue.toIntOrNull() ?: 0) * 3600000L +
                                (minutesInput.toIntOrNull() ?: 0) * 60000L +
                                (secondsInput.toIntOrNull() ?: 0) * 1000L +
                                (millisecondsInput.toIntOrNull() ?: 0)
                        onTimeValueChange(totalMillis)
                        updateTime()
                    }
                },
                label = { Text("Saat") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = false
            )

            // Dakika
            OutlinedTextField(
                value = minutesInput,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        minutesInput = newValue
                        val totalMillis = (hoursInput.toIntOrNull() ?: 0) * 3600000L +
                                (newValue.toIntOrNull() ?: 0) * 60000L +
                                (secondsInput.toIntOrNull() ?: 0) * 1000L +
                                (millisecondsInput.toIntOrNull() ?: 0)
                        onTimeValueChange(totalMillis)
                        updateTime()
                    }
                },
                label = { Text("Dakika") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = false
            )

            // Saniye
            OutlinedTextField(
                value = secondsInput,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        secondsInput = newValue
                        val totalMillis = (hoursInput.toIntOrNull() ?: 0) * 3600000L +
                                (minutesInput.toIntOrNull() ?: 0) * 60000L +
                                (newValue.toIntOrNull() ?: 0) * 1000L +
                                (millisecondsInput.toIntOrNull() ?: 0)
                        onTimeValueChange(totalMillis)
                        updateTime()
                    }
                },
                label = { Text("Saniye") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = false
            )

            // Milisaniye
            OutlinedTextField(
                value = millisecondsInput,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        millisecondsInput = newValue
                        val totalMillis = (hoursInput.toIntOrNull() ?: 0) * 3600000L +
                                (minutesInput.toIntOrNull() ?: 0) * 60000L +
                                (secondsInput.toIntOrNull() ?: 0) * 1000L +
                                (newValue.toIntOrNull() ?: 0)
                        onTimeValueChange(totalMillis)
                        updateTime()
                    }
                },
                label = { Text("Ms") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = false
            )
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSendCommand,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Komutu Gönder")
        }
    }
}
