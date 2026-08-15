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
import com.example.pravah.model.RoomModel
import com.example.pravah.viewmodel.AuthViewModel
import com.example.pravah.viewmodel.ClassViewModel

@Composable
fun ManageClassroom(viewModel: ClassViewModel = viewModel(), authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    var showAddStaffDialog by remember { mutableStateOf(false) }
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val instituteId = sharedPreferences.getString("instituteId", "")
    LaunchedEffect(Unit) {
        viewModel.getRoomsByInstitute(instituteId.toString())
    }
    val room = viewModel.rooms
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                room.forEach { room ->
                    Classroom(room.roomNo)
                }
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
                        showAddStaffDialog = true
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Button"
                        )
                    },
                    text = { Text("Add Staff") },
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
    if (showAddStaffDialog) {
        var roomNo by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        Dialog(
            onDismissRequest = {
                showAddStaffDialog = false
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).width(250.dp)
                ) {
                    Text(
                        text = "Add Staff",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = roomNo,
                        onValueChange = { roomNo = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = roomNo.isNotEmpty() && !authViewModel.isValidName(roomNo),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = email.isNotEmpty() && !authViewModel.isValidEmail(email),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                showAddStaffDialog = false
                            }
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (authViewModel.isValidName(roomNo) && authViewModel.isValidEmail(email)){
//                                    viewModel.addRoom(
//                                        institutionId = instituteId.toString(),
//                                        roomNo = roomNo,
//                                        devices = TODO(),
//                                    ){
//                                        Toast.makeText(context, "Room Added", Toast.LENGTH_SHORT).show()
//                                        viewModel.getRoomsByInstitute(instituteId.toString())
//                                        showAddStaffDialog = false
//                                    }
                                } else {
                                    Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
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

@Preview(showSystemUi = true)
@Composable
fun ManageClassroomPreview() {
}