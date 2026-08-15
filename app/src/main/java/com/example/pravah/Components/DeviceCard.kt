package com.example.pravah.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pravah.model.DeviceModel
import com.example.pravah.ui.theme.PravahAppTheme

@Composable
fun DeviceCard(onClick: () -> Unit = {}, device: DeviceModel) {
    val isActive = device.status.equals("Working", ignoreCase = true)
    val isOn = device.powerStatus.equals("ON", ignoreCase = true)

    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isActive) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        },
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Devices,
                    contentDescription = null,
                    tint = if (isActive) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = device.deviceName, // was "Room ${device.deviceName}" — leftover from RoomCard copy-paste
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Status: ${device.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            StatusPill(isOn = isOn, label = device.powerStatus, isActive = isActive)
        }
    }
}

@Composable
private fun StatusPill(isOn: Boolean, label: String?, isActive: Boolean) {

    val containerColor = if (isOn) {
        if (isActive) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (isOn) {
        if (isActive) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val dotColor = if (isOn) {
        if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    } else {
        Color.Gray
    }

    Row(
        modifier = Modifier
            .background(color = containerColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = dotColor, shape = CircleShape)
        )
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun DeviceCardPreview() {
    PravahAppTheme {
        DeviceCard(
            device = DeviceModel("AirConditioner1", "Working", "ON")
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceCardDamagedOnPreview() {
    PravahAppTheme {
        // Damaged but still drawing power — the case the old code couldn't distinguish
        DeviceCard(
            device = DeviceModel("AirConditioner2", "Damaged", "ON")
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceCardErrorPreview() {
    PravahAppTheme {
        DeviceCard(
            device = DeviceModel("AirConditioner3", "Damaged", "OFF")
        )
    }
}
