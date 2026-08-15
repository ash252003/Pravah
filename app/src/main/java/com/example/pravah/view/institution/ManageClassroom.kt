package com.example.pravah.view.institution

import android.content.Context
import android.content.SharedPreferences
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
fun ManageClassroom(viewModel: ClassViewModel = viewModel(), authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    var showAddRoomDialog by remember { mutableStateOf(false) }
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val instituteId = sharedPreferences.getString("instituteId", "")
    // Device count
    var deviceCountText by remember { mutableStateOf("1") }
    var deviceCount by remember { mutableStateOf(1) }
    LaunchedEffect(Unit) {
        viewModel.getRoomsByInstitute(instituteId.toString())
    }
    val room = viewModel.rooms
    Box(modifier = Modifier.fillMaxSize()) {
        if (room.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    room.forEach { room ->
                        Classroom(
                            roomId = room.id,
                            roomNo = room.roomNo,
                            onDelete = { roomId ->
                                viewModel.deleteRoom(roomId){
                                    viewModel.getRoomsByInstitute(instituteId.toString())
                                    Toast.makeText(context, "Room Deleted Successfully", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text("No Data Found")
            }
        }
        BottomAppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 0.dp, horizontal = 16.dp),
            containerColor = Color.Transparent
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(color = Color.Transparent),
                horizontalArrangement = Arrangement.End
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        showAddRoomDialog = true
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Button"
                        )
                    },
                    text = { Text("Add Class") },
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
    if (showAddRoomDialog) {
        var roomNo by remember { mutableStateOf("") }
        var roomStatus by remember { mutableStateOf("empty") }

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
                shape = RoundedCornerShape(16.dp)
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

                        Spacer(modifier = Modifier.height(20.dp))

                        // Room Number
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
                            isError = roomNo.isNotEmpty() && !authViewModel.isValidName(roomNo)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = deviceCountText,
                            onValueChange = { value ->
                                if (value.all { it.isDigit() }) {
                                    deviceCountText = value
                                    val count = value.toIntOrNull()
                                    if (count != null && count in 1..20) {
                                        deviceCount = count
                                        while (deviceNames.size < count) {
                                            deviceNames.add("")
                                        }
                                        while (deviceNames.size > count) {
                                            deviceNames.removeAt(deviceNames.lastIndex)
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

                        Spacer(modifier = Modifier.height(12.dp))

                        // Device fields
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
                                singleLine = true,
                                isError = deviceName.isNotEmpty() && !authViewModel.isValidName(deviceName)
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if (
                                        roomNo.isBlank() &&
                                        deviceNames.any { it.isBlank() }
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "Please fill all fields",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }
                                    val devices = deviceNames.map { espId ->
                                        DeviceModel(
                                            id = "",
                                            classId = "",
                                            deviceName = espId,
                                            status = "working",
                                            powerStatus = null
                                        )
                                    }

                                    viewModel.addRoom(
                                        institutionId = instituteId.toString(),
                                        roomNo = roomNo,
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
}

@Preview(showSystemUi = true)
@Composable
fun ManageClassroomPreview() {
}