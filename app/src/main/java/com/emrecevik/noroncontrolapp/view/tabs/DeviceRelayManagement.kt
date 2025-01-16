package com.emrecevik.noroncontrolapp.view.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.emrecevik.noroncontrolapp.R
import com.emrecevik.noroncontrolapp.components.TimedCommandSheet
import com.emrecevik.noroncontrolapp.model.Relay
import com.emrecevik.noroncontrolapp.model.requestBody.SetRelay
import com.emrecevik.noroncontrolapp.viewmodel.RelayViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceRelayManagementTab(deviceId: Long) {
    val relayVM: RelayViewModel = viewModel()

    val relayList = listOf(
        Relay(R.drawable.power, "Röle"),
        Relay(R.drawable.macro, "Macro"),
        Relay(R.drawable.device_info, "Fan"),
    )

    var timeValue by remember { mutableLongStateOf(0L) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by relayVM.setRelayLoading.collectAsState()
    val relayResponse by relayVM.relayResponse.collectAsState()
    val errorMessageState by relayVM.errorMessage.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    var selectedIndex by remember { mutableIntStateOf(-1) }
    var showAnimation by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var isConnectionError by remember { mutableStateOf(false) }
    var detailedErrorMessage by remember { mutableStateOf("") }

    val successComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    val errorComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    val connectionErrorComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.connection_error))

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else
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
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(relayList.size) { index ->
                            val relay = relayList[index]
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .aspectRatio(1f)
                                    .clickable {
                                        selectedIndex = index
                                        coroutineScope.launch {
                                            bottomSheetState.show()
                                        }
                                    },
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        painter = painterResource(id = relay.icon),
                                        contentDescription = relay.name,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = relay.name,
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (bottomSheetState.isVisible) {
                ModalBottomSheet(
                    onDismissRequest = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    },
                    sheetState = bottomSheetState,
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                    content = {
                        when (selectedIndex) {
                            0 -> TimedCommandSheet(
                                timeValue = timeValue,
                                onTimeValueChange = { newValue -> timeValue = newValue },
                                errorMessage = errorMessage,
                                onSendCommand = { relayId, time ->
                                    coroutineScope.launch {
                                        val setRelay = SetRelay(
                                            setrelay = relayId,
                                            time = time.toString(),
                                            type = "2",
                                            deviceId = deviceId.toInt()
                                        )
                                        relayVM.setRelay(setRelay)
                                        bottomSheetState.hide()
                                    }
                                },
                                deviceId = deviceId
                            )

                            1 -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Özel Komut Gönder",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    var customCommand by remember { mutableStateOf("") }
                                    var customError by remember { mutableStateOf<String?>(null) }

                                    OutlinedTextField(
                                        value = customCommand,
                                        onValueChange = { newValue ->
                                            customCommand = newValue
                                            // Özel doğrulamalar ekleyebilirsiniz
                                        },
                                        label = { Text("Özel Komut Girin") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        isError = customError != null,
                                        singleLine = true
                                    )

                                    if (customError != null) {
                                        Text(
                                            text = customError!!,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            // Özel komut gönderme işlemleri
                                            // Örneğin, relayVM'e özel bir metot çağırabilirsiniz
                                            // relayVM.sendCustomCommand(customCommand)
                                            coroutineScope.launch {
                                                bottomSheetState.hide()
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Özel Komutu Gönder")
                                    }
                                }
                            }
                            // Diğer indexler için ek durumlar ekleyebilirsiniz
                            else -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Bu özellik henüz desteklenmiyor.",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                )
            }

            if (showAnimation) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        .align(Alignment.Center)
                ) {
                    val composition = when {
                        isConnectionError -> connectionErrorComposition
                        isSuccess -> successComposition
                        else -> errorComposition
                    }
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LottieAnimation(
                            composition = composition,
                            iterations = 1,
                            modifier = Modifier.size(200.dp)
                        )
                        Text(
                            text = when {
                                isSuccess -> "Komut başarıyla gönderildi."
                                isConnectionError -> "Bağlantı hatası. Lütfen cihazı kontrol edin."
                                else -> "Komut gönderilemedi."
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = when {
                                isSuccess -> MaterialTheme.colorScheme.primary
                                isConnectionError -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.error
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        if (!isSuccess && detailedErrorMessage.isNotEmpty()) {
                            Text(
                                text = detailedErrorMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Gelen Yanıtları İzleme ve Animasyon Gösterimi
            LaunchedEffect(relayResponse, errorMessageState) {
                if (relayResponse != null) {
                    isSuccess = true
                    isConnectionError = false
                    showAnimation = true
                } else if (errorMessageState != null) {
                    isSuccess = false
                    isConnectionError =
                        errorMessageState!!.contains("Connection failed", ignoreCase = true)
                    detailedErrorMessage = errorMessageState.toString()
                    showAnimation = true
                }
                if (showAnimation) {
                    delay(3000) // 3 saniye
                    showAnimation = false
                }
            }
        }
}
