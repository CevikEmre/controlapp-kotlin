package com.emrecevik.noroncontrolapp.view.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.emrecevik.noroncontrolapp.R
import com.emrecevik.noroncontrolapp.model.requestBody.DeviceInfoBody
import com.emrecevik.noroncontrolapp.viewmodel.RelayViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoTab(deviceId: Long) {
    val relayVM: RelayViewModel = viewModel()
    val deviceInfo = relayVM.deviceInfoResponse.collectAsState()
    val deviceInfoLoading = relayVM.deviceInfoLoading.collectAsState()

    LaunchedEffect(Unit) {
        relayVM.getDeviceInfo(deviceInfoBody = DeviceInfoBody(deviceId, "1"))
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = deviceInfoLoading.value),
        onRefresh = {
            relayVM.getDeviceInfo(deviceInfoBody = DeviceInfoBody(deviceId, "1"))
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                deviceInfo.value?.let { info ->
                    if (info.inp1 == "1") {
                        LottieAnimationSection(animationResId = R.raw.engine_start)
                        Text(
                            text = "Motor Çalışıyor",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "Motor Çalışmıyor",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    // Cihaz bilgileri
                    DeviceInfoRow(label = "Input 1", value = "Duration: ${formatTime(info.time1)}")
                    DeviceInfoRow(label = "Input 2", value = "Duration: ${formatTime(info.time2)}")
                    DeviceInfoRow(label = "Input 3", value = "Duration: ${formatTime(info.time3)}")
                    DeviceInfoRow(label = "Relay 1", value = info.relay1)
                    DeviceInfoRow(label = "Relay 2", value = info.relay2)
                    DeviceInfoRow(label = "Relay 3", value = info.relay3)
                } ?: run {
                    Text(
                        text = "Cihaz Bağlantısı bulunamadı",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun LottieAnimationSection(animationResId: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@SuppressLint("DefaultLocale")
fun formatTime(seconds: String): String {
    return try {
        val totalSeconds = seconds.toInt()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val secs = totalSeconds % 60
        String.format("%02d:%02d:%02d", hours, minutes, secs)
    } catch (e: NumberFormatException) {
        "00:00:00"
    }
}

@Composable
fun DeviceInfoRow(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
