package com.emrecevik.noroncontrolapp.view.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emrecevik.noroncontrolapp.components.DetailCard
import com.emrecevik.noroncontrolapp.model.response.Devices



@Composable
fun DeviceDetailTab(device: Devices) {
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
                .padding(16.dp)
        ) {
            Text(
                text = "Cihaz Detayları",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Kaydırılabilir Detay Listesi
            val detailItems = listOf(
                "Yetki" to (if (device.isAdmin) "Admin" else "Kullanıcı"),
                "Cihaz ID" to (device.devId?.toString() ?: "Bilinmiyor"),
                "Cihaz Tipi" to (device.deviceType ?: "Bilinmiyor"),
                "Durum" to (if (device.connected == true) "Bağlı" else "Bağlı Değil"),
                "M2M Numarası" to (device.m2mNumber ?: "Bilinmiyor"),
                "M2M Seri Numarası" to (device.m2mSerial ?: "Bilinmiyor"),
                "Oluşturulma Tarihi" to (device.createdDateTime ?: "Bilinmiyor"),
                "Aktivasyon Tarihi" to (device.activatedDateTime ?: "Bilinmiyor"),
                "Aktif Gün Sayısı" to (device.activeDays?.toString() ?: "Bilinmiyor"),
                "Yıllık Ücret" to (if (device.yearlyPrice != null) "${device.yearlyPrice} $" else "Bilinmiyor")
            )


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Kartlar arasında boşluk
            ) {
                items(detailItems.size) {
                    val item = detailItems[it]
                    DetailCard(label = item.first, value = item.second)
                }
            }

        }
    }
}