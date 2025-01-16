package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.emrecevik.noroncontrolapp.components.DeviceCard
import com.emrecevik.noroncontrolapp.viewmodel.DeviceViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceSelection(navController: NavController) {
    val deviceVM: DeviceViewModel = viewModel()
    val devices = deviceVM.devices.collectAsState()

    LaunchedEffect(Unit) {
        deviceVM.fetchDevices()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Ayarlar")
                }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               when{
                   devices.value.isNullOrEmpty() -> {
                       item {
                           Text("Yüklenecek cihaz yok")
                       }
                   }
               }
                items(devices.value!!.size) { index ->
                    val device = devices.value!![index]
                    DeviceCard(device,navController)
                }
            }
        }
    )
}
