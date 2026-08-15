package com.example.pravah.view.Staff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pravah.model.DeviceModel
import com.example.pravah.ui.theme.PravahAppTheme
import com.example.pravah.viewmodel.ClassViewModel


@Composable
fun DeviceInfoScreen(
    deviceId: String,
    viewModel: ClassViewModel,
) {
    val device = viewModel.rooms
        .flatMap { it.devices }
        .firstOrNull { it.id == deviceId }

    if (device != null) {
        DeviceInfo(
            device = device,
            onTogglePower = { isOn -> viewModel.toggleDevicePower(deviceId, isOn) }
        )
    } else {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (viewModel.isLoading) "Loading device..." else "Device not found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun DeviceInfo(device: DeviceModel, onTogglePower: (Boolean) -> Unit) {
    val isActive = device.status.equals("Working", ignoreCase = true)

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                DeviceInfoHeader(device = device, isActive = isActive)
            }
            item {
                PowerStatusRow(device = device, onTogglePower = onTogglePower)
            }
        }
    }
}

@Composable
private fun DeviceInfoHeader(device: DeviceModel, isActive: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
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
            Text(
                text = device.deviceName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = device.status,
            style = MaterialTheme.typography.headlineSmall,
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun PowerStatusRow(device: DeviceModel, onTogglePower: (Boolean) -> Unit) {
    val isOn = device.powerStatus.equals("ON", ignoreCase = true) // powerStatus is String? — safe on null

    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Power",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isOn) "ON" else "OFF",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = isOn,
                onCheckedChange = onTogglePower, // -> viewModel.toggleDevicePower -> Firestore
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceInfoPreviewOn() {

    var previewDevice by remember {
        mutableStateOf(DeviceModel(id = "d1", classId = "1", deviceName = "AirConditioner1", status = "Working", powerStatus = "ON"))
    }
    PravahAppTheme {
        DeviceInfo(
            device = previewDevice,
            onTogglePower = { isOn -> previewDevice = previewDevice.copy(powerStatus = if (isOn) "ON" else "OFF") }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceInfoPreviewOff() {
    var previewDevice by remember {
        mutableStateOf(DeviceModel(id = "d2", classId = "1", deviceName = "AirConditioner2", status = "Damaged", powerStatus = "OFF"))
    }
    PravahAppTheme {
        DeviceInfo(
            device = previewDevice,
            onTogglePower = { isOn -> previewDevice = previewDevice.copy(powerStatus = if (isOn) "ON" else "OFF") }
        )
    }
}
