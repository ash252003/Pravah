package com.example.pravah.view.Staff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pravah.Components.DeviceCard
import com.example.pravah.model.DeviceModel
import com.example.pravah.model.RoomModel
import com.example.pravah.ui.theme.PravahAppTheme
import com.example.pravah.viewmodel.ClassViewModel

/**
 * Screen-level wrapper: looks up `roomId` inside the already-loaded
 * `viewModel.rooms` (shared from the nav graph — see PravahNavGraph) and
 * owns navigating onward to deviceInfo when a device is tapped.
 *
 * If the room isn't found (e.g. a deep link before rooms finish loading,
 * or a stale/bad id), shows a simple message instead of a blank screen.
 */
@Composable
fun RoomInfoScreen(
    roomId: String,
    navController: NavController,
    viewModel: ClassViewModel,
) {
    val room = viewModel.rooms.firstOrNull { it.id == roomId }

    if (room != null) {
        RoomInfo(
            room = room,
            // Literal route string — must match the "deviceInfo/{deviceId}"
            // pattern registered in UserNavigation.kt's NavHost.
            onDeviceClick = { device -> navController.navigate("deviceInfo/${device.id}") }
        )
    } else {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (viewModel.isLoading) "Loading room..." else "Room not found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Pure/stateless: renders a room + its devices (room.devices — RoomModel
 * carries its own device list, no separate `devices` param needed) and
 * reports device taps up via `onDeviceClick`.
 */
@Composable
fun RoomInfo(room: RoomModel, onDeviceClick: (DeviceModel) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                RoomInfoBlock(room = room)
            }
            items(
                items = room.devices,
                key = { it.id }
            ) { device ->
                DeviceCard(device = device, onClick = { onDeviceClick(device) })
            }
        }
    }
}

@Composable
fun RoomInfoBlock(room: RoomModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MeetingRoom,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Text(
                text = "Room ${room.roomNo}",
                // was displayLarge (57sp) — next to a 64dp icon that's
                // oversized enough to wrap on narrower screens; headlineMedium
                // keeps it prominent without crowding the icon
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = room.status,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RoomInfoPreview() {
    PravahAppTheme {
        RoomInfo(
            room = RoomModel(
                id = "1",
                institutionId = "1",
                roomNo = "701",
                status = "Active",
                devices = listOf(
                    DeviceModel(id = "d1", classId = "1", deviceName = "AirConditioner1", status = "Working", powerStatus = "ON"),
                    DeviceModel(id = "d2", classId = "1", deviceName = "AirConditioner2", status = "Damaged", powerStatus = "ON"),
                    DeviceModel(id = "d3", classId = "1", deviceName = "AirConditioner3", status = "Working", powerStatus = "OFF"),
                )
            ),
            onDeviceClick = {}
        )
    }
}
