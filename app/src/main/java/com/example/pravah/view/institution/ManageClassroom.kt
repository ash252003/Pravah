package com.example.pravah.view.institution

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pravah.Components.Classroom
import com.example.pravah.model.DeviceModel
import com.example.pravah.model.RoomModel
import com.example.pravah.viewmodel.AuthViewModel
import com.example.pravah.viewmodel.ClassViewModel

@Composable
fun ManageClassroom(
    viewModel: ClassViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {

    val context = LocalContext.current

    var showAddRoomDialog by remember {
        mutableStateOf(false)
    }

    // Selected room for editing
    var selectedRoom by remember {
        mutableStateOf<RoomModel?>(null)
    }

    val sharedPreferences = context.getSharedPreferences(
        "user_session",
        Context.MODE_PRIVATE
    )

    val instituteId = sharedPreferences.getString(
        "instituteId",
        ""
    )

    LaunchedEffect(Unit) {
        viewModel.getRoomsByInstitute(
            instituteId.toString()
        )
    }

    val rooms = viewModel.rooms

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // =========================================================
        // ROOM LIST
        // =========================================================

        if (rooms.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 100.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 4.dp,
                                end = 4.dp,
                                bottom = 4.dp
                            )
                    ) {

                        Text(
                            text = "Classrooms",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "${rooms.size} room" +
                                    if (rooms.size != 1) "s" else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                item {

                    rooms.forEach { room ->

                        Classroom(
                            roomId = room.id,
                            roomNo = room.roomNo,
                            deviceCount = room.devices.size,

                            // =================================================
                            // EDIT
                            // =================================================

                            onEdit = {

                                selectedRoom = room

                            },

                            // =================================================
                            // DELETE
                            // =================================================

                            onDelete = { roomId ->

                                viewModel.deleteRoom(roomId) {

                                    viewModel.getRoomsByInstitute(
                                        instituteId.toString()
                                    )

                                    Toast.makeText(
                                        context,
                                        "Room Deleted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    }
                }
            }

        } else {

            // =========================================================
            // EMPTY STATE
            // =========================================================

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "No Classrooms Found",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Add a classroom to get started.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // =============================================================
        // ADD CLASS BUTTON
        // =============================================================

        BottomAppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    horizontal = 16.dp
                ),
            containerColor = Color.Transparent
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.End
            ) {

                ExtendedFloatingActionButton(
                    onClick = {
                        showAddRoomDialog = true
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Classroom"
                        )
                    },
                    text = {
                        Text("Add Class")
                    },
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    // =================================================================
    // ADD CLASSROOM DIALOG
    // =================================================================

    if (showAddRoomDialog) {

        var roomNo by remember {
            mutableStateOf("")
        }

        var deviceCountText by remember {
            mutableStateOf("1")
        }

        val deviceNames = remember {
            mutableStateListOf("")
        }

        Dialog(
            onDismissRequest = {
                showAddRoomDialog = false
            }
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .padding(20.dp)
                        .width(300.dp)
                ) {

                    item {

                        Text(
                            text = "Add Classroom",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        // Room number

                        OutlinedTextField(
                            value = roomNo,
                            onValueChange = {
                                roomNo = it
                            },
                            label = {
                                Text("Room Number")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = roomNo.isNotEmpty() &&
                                    !authViewModel.isValidName(roomNo)
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        // Device count

                        OutlinedTextField(
                            value = deviceCountText,
                            onValueChange = { value ->

                                if (
                                    value.isEmpty() ||
                                    value.all { it.isDigit() }
                                ) {

                                    val count =
                                        value.toIntOrNull()

                                    if (
                                        value.isEmpty() ||
                                        (count != null && count in 1..20)
                                    ) {

                                        deviceCountText = value

                                        if (count != null) {

                                            while (
                                                deviceNames.size < count
                                            ) {
                                                deviceNames.add("")
                                            }

                                            while (
                                                deviceNames.size > count
                                            ) {
                                                deviceNames.removeAt(
                                                    deviceNames.lastIndex
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            label = {
                                Text("Number of Devices")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        // ESP IDs

                        deviceNames.forEachIndexed { index, deviceName ->

                            OutlinedTextField(
                                value = deviceName,
                                onValueChange = {
                                    deviceNames[index] = it
                                },
                                label = {
                                    Text("ESP ID ${index + 1}")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            TextButton(
                                onClick = {
                                    showAddRoomDialog = false
                                }
                            ) {
                                Text("Cancel")
                            }

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Button(
                                onClick = {

                                    if (
                                        roomNo.isBlank() ||
                                        deviceNames.any {
                                            it.isBlank()
                                        }
                                    ) {

                                        Toast.makeText(
                                            context,
                                            "Please fill all fields",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@Button
                                    }

                                    val devices =
                                        deviceNames.map { espId ->

                                            DeviceModel(
                                                id = "",
                                                classId = "",
                                                deviceName = espId,
                                                status = "working",
                                                powerStatus = null
                                            )
                                        }

                                    viewModel.addRoom(
                                        institutionId =
                                            instituteId.toString(),
                                        roomNo = roomNo.trim(),
                                        roomStatus = "empty",
                                        devices = devices
                                    ) { success ->

                                        if (success) {

                                            Toast.makeText(
                                                context,
                                                "Room Added Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            viewModel.getRoomsByInstitute(
                                                instituteId.toString()
                                            )

                                            showAddRoomDialog = false

                                        } else {

                                            Toast.makeText(
                                                context,
                                                "Failed to add room",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            ) {

                                Text("Add")
                            }
                        }
                    }
                }
            }
        }
    }

    // =================================================================
    // EDIT CLASSROOM DIALOG
    // =================================================================

    selectedRoom?.let { currentRoom ->

        var editRoomNo by remember(
            currentRoom.id
        ) {
            mutableStateOf(currentRoom.roomNo)
        }

        val editDeviceNames = remember(
            currentRoom.id
        ) {
            mutableStateListOf<String>().apply {

                addAll(
                    currentRoom.devices.map {
                        it.deviceName
                    }
                )
            }
        }

        var editDeviceCountText by remember(
            currentRoom.id
        ) {
            mutableStateOf(
                currentRoom.devices.size.toString()
            )
        }

        Dialog(
            onDismissRequest = {
                selectedRoom = null
            }
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .padding(20.dp)
                        .width(300.dp)
                ) {

                    item {

                        Text(
                            text = "Edit Classroom",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        // =================================================
                        // ROOM NUMBER
                        // =================================================

                        OutlinedTextField(
                            value = editRoomNo,
                            onValueChange = {
                                editRoomNo = it
                            },
                            label = {
                                Text("Room Number")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        // =================================================
                        // DEVICE COUNT
                        // =================================================

                        OutlinedTextField(
                            value = editDeviceCountText,
                            onValueChange = { value ->

                                if (
                                    value.isEmpty() ||
                                    value.all { it.isDigit() }
                                ) {

                                    val count =
                                        value.toIntOrNull()

                                    if (
                                        value.isEmpty() ||
                                        (count != null && count in 1..20)
                                    ) {

                                        editDeviceCountText = value

                                        if (count != null) {

                                            while (
                                                editDeviceNames.size < count
                                            ) {
                                                editDeviceNames.add("")
                                            }

                                            while (
                                                editDeviceNames.size > count
                                            ) {
                                                editDeviceNames.removeAt(
                                                    editDeviceNames.lastIndex
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            label = {
                                Text("Number of Devices")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        // =================================================
                        // ESP IDs
                        // =================================================

                        editDeviceNames.forEachIndexed {
                                index,
                                deviceName ->

                            OutlinedTextField(
                                value = deviceName,
                                onValueChange = {
                                    editDeviceNames[index] = it
                                },
                                label = {
                                    Text("ESP ID ${index + 1}")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        // =================================================
                        // BUTTONS
                        // =================================================

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            TextButton(
                                onClick = {
                                    selectedRoom = null
                                }
                            ) {
                                Text("Cancel")
                            }

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Button(
                                onClick = {

                                    // Validate room
                                    if (
                                        editRoomNo.isBlank()
                                    ) {

                                        Toast.makeText(
                                            context,
                                            "Please enter room number",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@Button
                                    }

                                    // Validate devices
                                    if (
                                        editDeviceNames.isEmpty() ||
                                        editDeviceNames.any {
                                            it.isBlank()
                                        }
                                    ) {

                                        Toast.makeText(
                                            context,
                                            "Please fill all device IDs",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@Button
                                    }

                                    val updatedDevices =
                                        editDeviceNames.map { espId ->

                                            DeviceModel(
                                                id = "",
                                                classId = currentRoom.id,
                                                deviceName = espId.trim(),
                                                status = "working",
                                                powerStatus = null
                                            )
                                        }

                                    viewModel.editRoom(
                                        roomId = currentRoom.id,
                                        roomNo = editRoomNo.trim(),
                                        devices = updatedDevices,
                                        institutionId =
                                            instituteId.toString()
                                    ) { success ->

                                        if (success) {

                                            Toast.makeText(
                                                context,
                                                "Room Updated Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            selectedRoom = null

                                            viewModel.getRoomsByInstitute(
                                                instituteId.toString()
                                            )

                                        } else {

                                            Toast.makeText(
                                                context,
                                                "Failed to update room",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            ) {

                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ManageClassroomPreview() {
}