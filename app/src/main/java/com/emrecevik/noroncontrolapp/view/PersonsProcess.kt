package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.emrecevik.noroncontrolapp.R
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonsProcess(deviceId: Long) {
    val deviceVM: DeviceViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val tfValue = remember { mutableStateOf("") }

    val isSuccess = deviceVM.isSuccess.collectAsState()
    val isError = deviceVM.isError.collectAsState()
    val isRefreshing = deviceVM.isRefreshing
    val errorMessage = deviceVM.addUserErrorMessage.collectAsState()

    val successComposition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    val errorComposition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))

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

                // Hata mesajını TextField'ın altında göster
                if (isError.value && errorMessage.value != null) {
                    Text(
                        text = errorMessage.value ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Ekleme Butonu
                Button(
                    onClick = {
                        scope.launch {
                            deviceVM.addUserToDevice(
                                devId = deviceId,
                                phone = tfValue.value
                            )
                            kotlinx.coroutines.delay(3000)
                            deviceVM.resetStates()
                        }
                    },
                    enabled = !isRefreshing.value // Yükleme sırasında butonu devre dışı bırak
                ) {
                    Text("Cihaz Ekle")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Yükleme durumu
                if (isRefreshing.value) {
                    Text(
                        text = "İşlem devam ediyor...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Başarı veya hata animasyonu
                if (isSuccess.value || isError.value) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LottieAnimation(
                            composition = if (isSuccess.value) successComposition.value else errorComposition.value,
                            iterations = 1,
                            modifier = Modifier.size(200.dp)
                        )

                        Text(
                            text = when {
                                isSuccess.value -> "Cihaz Eklemesi Başarılı"
                                isError.value -> errorMessage.value ?: "Bir hata oluştu."
                                else -> ""
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = when {
                                isSuccess.value -> MaterialTheme.colorScheme.primary
                                isError.value -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    )
}




