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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pravah.R
import com.example.pravah.viewmodel.AuthViewModel
import com.example.pravah.viewmodel.UserViewModel

@Composable
fun ManageStaff(viewModel: UserViewModel = viewModel(), authViewModel: AuthViewModel = viewModel()) {
    var showAddStaffDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val instituteName = sharedPreferences.getString("name", "")
    LaunchedEffect(Unit) {
        viewModel.getAllStaff(instituteName.toString())
    }
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect{ msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
    val staff = viewModel.staff
    Box(modifier = Modifier.fillMaxSize()) {
        if (staff.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Name",
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Email",
                                    modifier = Modifier.weight(2f),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Box(
                                    modifier = Modifier.weight(0.6f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Action")
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            staff.forEach { staff ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = staff.name,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = staff.email,
                                        modifier = Modifier.weight(2f)
                                    )

                                    Box(
                                        modifier = Modifier.weight(0.5f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "More options",
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
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
        var name by remember { mutableStateOf("") }
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
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = name.isNotEmpty() && !authViewModel.isValidName(name),
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
                                if (authViewModel.isValidName(name) && authViewModel.isValidEmail(email)){
                                    val password = generatePassword()
                                    viewModel.addStaff(
                                        name = name.trim(),
                                        email = email.trim(),
                                        password = password,
                                        instituteName = instituteName.toString(),
                                    ){
                                        Toast.makeText(context, "Staff Added", Toast.LENGTH_SHORT).show()
                                        showAddStaffDialog = false
                                    }
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

fun generatePassword(): String {
    val chars = ('a'..'z') +
            ('A'..'Z') +
            ('0'..'9')

    return List(8) {
        chars.random()
    }.joinToString("")
}

@Preview(showSystemUi = true)
@Composable
fun ManageStaffPreview() {
    ManageStaff()
}