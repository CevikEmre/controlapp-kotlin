package com.emrecevik.noroncontrolapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.emrecevik.noroncontrolapp.model.response.Devices
import com.emrecevik.noroncontrolapp.navigation.Screen

@Composable
fun DeviceCard(device: Devices?, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                device?.devId?.let { devId ->
                    navController.navigate("${Screen.DeviceSettings.screen}/$devId")
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Cihaz ID: ${device?.devId}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Durum: ${if (device?.enable == true) "Aktif" else "Pasif"}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (device?.enable == true) Color.Green else Color.Red
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tip: ${device?.deviceType}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "M2M Numarası: ${device?.m2mNumber ?: "Belirtilmedi"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
