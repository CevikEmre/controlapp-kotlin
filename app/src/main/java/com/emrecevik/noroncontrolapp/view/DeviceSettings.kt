package com.emrecevik.noroncontrolapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.emrecevik.noroncontrolapp.R
import com.emrecevik.noroncontrolapp.components.SettingsCard
import com.emrecevik.noroncontrolapp.model.Relay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceSettings(navController: NavController, deviceId: Long) {
    val cards = listOf(
        Relay(R.drawable.power, "Röle"),
        Relay(R.drawable.macro, "Macro"),
        Relay(R.drawable.device_info, "Fan"),
    )

    var isBottomSheetVisible = remember { mutableStateOf(false) }
    var selectedIndex = remember { mutableStateOf<Int?>(null) } // Seçilen kartın indeksini saklıyoruz.

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cihaz Ayarları") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                }
            )
        },
        content = { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cards.size) { index ->
                        val card = cards[index]
                        SettingsCard(card) {
                            selectedIndex.value = index // Seçilen kartın indeksini ayarla.
                            isBottomSheetVisible.value = true
                        }
                    }
                }

                if (isBottomSheetVisible.value) {
                    ModalBottomSheet(
                        onDismissRequest = { isBottomSheetVisible.value = false },
                        sheetState = rememberModalBottomSheetState(),
                        shape = RoundedCornerShape(12.dp),
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        selectedIndex.value?.let { index ->
                            when (index) {
                                0 -> { // Röle kartı seçildiğinde
                                    UpdateRelays(deviceId)
                                }


                                else -> {
                                    Text(
                                        text = "Bu özellik henüz aktif değil.",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
