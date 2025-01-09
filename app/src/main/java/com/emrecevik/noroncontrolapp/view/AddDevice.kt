package com.emrecevik.noroncontrolapp.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.emrecevik.noroncontrolapp.NoroncontrolappApplication
import com.emrecevik.noroncontrolapp.R
import com.emrecevik.noroncontrolapp.session.SessionManager
import com.emrecevik.noroncontrolapp.viewmodel.ClientViewModel
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScreen(navController: NavController) {
    val clientVM: ClientViewModel = viewModel()
    val sessionManager = SessionManager(context = NoroncontrolappApplication.Companion.appContext())
    val token = sessionManager.getAccessToken()
    val deviceVM: DeviceViewModel = viewModel()
    val clientDetails = clientVM.clientDetails.collectAsState()
    val scope = rememberCoroutineScope()
    val tfValue = remember { mutableStateOf<String>("") }

    val isSuccess = deviceVM.addSuccess.collectAsState()
    val isError = deviceVM.addError.collectAsState()
    val errorMessage = deviceVM.addUserErrorMessage.collectAsState()

    val successComposition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    val errorComposition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))

    LaunchedEffect(Unit) {
        clientVM.getClientDetails(token)
    }

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = tfValue.value,
                    onValueChange = {
                        if (it.isDigitsOnly()) {
                            tfValue.value = it
                        }
                    },
                    modifier = Modifier.padding(6.dp),
                    label = { Text("Cihaz Adı") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text("Örnek:10000001")
                    }
                )

                if (isError.value && errorMessage.value != null) {
                    Text(
                        text = errorMessage.value ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val deviceId = tfValue.value.toLongOrNull()
                                val phone = clientDetails.value?.phone.orEmpty()
                                if (deviceId != null && phone.isNotEmpty()) {
                                    deviceVM.addUserToDevice(deviceId, phone)
                                    delay(3000)
                                    deviceVM.resetStates()
                                } else {
                                    deviceVM.addUserErrorMessage.value = "Geçersiz cihaz ID veya telefon numarası."
                                }
                            } catch (e: Exception) {
                                Log.e("AddDeviceScreen", "Error: ${e.localizedMessage}")
                            }
                        }
                    },
                    enabled = !deviceVM.isRefreshing.value
                ) {
                    Text("Cihaz Ekle")
                }

                Spacer(modifier = Modifier.height(24.dp))

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
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Yeni Cihaz Ekle")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
            )
        }
    )
}
