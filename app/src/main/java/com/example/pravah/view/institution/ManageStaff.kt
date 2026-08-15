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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pravah.viewmodel.AuthViewModel
import com.example.pravah.viewmodel.UserViewModel

@Composable
fun ManageStaff(
    viewModel: UserViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    var showAddStaffDialog by remember {
        mutableStateOf(false)
    }

    var expandedMenu by remember {
        mutableStateOf<String?>(null)
    }

    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences(
        "user_session",
        Context.MODE_PRIVATE
    )

    val instituteId = sharedPreferences.getString(
        "instituteId",
        ""
    )

    /*
     * Get staff when screen opens
     */
    LaunchedEffect(Unit) {
        viewModel.getAllStaff(instituteId.toString())
    }

    /*
     * Observe ViewModel toast messages
     */
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { msg ->
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val staff = viewModel.staff

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        /*
         * =========================
         * STAFF LIST
         * =========================
         */
        if (staff.isNotEmpty()) {

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

                /*
                 * Header
                 */
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
                            text = "Staff Members",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "${staff.size} staff member" +
                                    if (staff.size != 1) "s" else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                /*
                 * Staff cards
                 */
                items(
                    items = staff,
                    key = { member ->
                        member.email
                    }
                ) { member ->

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 2.dp
                        ),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 14.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            /*
                             * Avatar
                             */
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = MaterialTheme
                                            .colorScheme
                                            .primaryContainer,
                                        shape = RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = member.name
                                        .trim()
                                        .firstOrNull()
                                        ?.uppercase()
                                        ?: "?",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme
                                        .colorScheme
                                        .onPrimaryContainer
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(14.dp)
                            )

                            /*
                             * Name + Email
                             */
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = member.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme
                                        .colorScheme
                                        .onSurface
                                )

                                Spacer(
                                    modifier = Modifier.height(3.dp)
                                )

                                Text(
                                    text = member.email,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant,
                                    maxLines = 1
                                )
                            }

                            /*
                             * More menu
                             */
                            Box {

                                IconButton(
                                    onClick = {
                                        expandedMenu = member.email
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More options",
                                        tint = MaterialTheme
                                            .colorScheme
                                            .onSurfaceVariant
                                    )
                                }

                                DropdownMenu(
                                    expanded = expandedMenu == member.email,
                                    onDismissRequest = {
                                        expandedMenu = null
                                    }
                                ) {

                                    DropdownMenuItem(
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = Color.Red
                                            )
                                        },
                                        text = {
                                            Text(
                                                text = "Delete",
                                                color = Color.Red
                                            )
                                        },
                                        onClick = {

                                            expandedMenu = null

                                            viewModel.deleteStaff(
                                                member.email,
                                                instituteId.toString()
                                            ) {

                                                viewModel.getAllStaff(
                                                    instituteId.toString()
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

        } else {

            /*
             * =========================
             * EMPTY STATE
             * =========================
             */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 70.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .background(
                            color = MaterialTheme
                                .colorScheme
                                .primaryContainer,
                            shape = RoundedCornerShape(22.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme
                            .colorScheme
                            .onPrimaryContainer
                    )
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "No Staff Added",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Add staff members to manage " +
                            "your institution.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme
                        .colorScheme
                        .onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        /*
         * =========================
         * ADD STAFF FAB
         * =========================
         */
        ExtendedFloatingActionButton(
            onClick = {
                showAddStaffDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Staff"
                )
            },
            text = {
                Text("Add Staff")
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }

    /*
     * =========================
     * ADD STAFF DIALOG
     * =========================
     */
    if (showAddStaffDialog) {

        var name by remember {
            mutableStateOf("")
        }

        var email by remember {
            mutableStateOf("")
        }

        Dialog(
            onDismissRequest = {
                showAddStaffDialog = false
            }
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme
                        .colorScheme
                        .surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    /*
                     * Dialog title
                     */
                    Text(
                        text = "Add Staff",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "Add a staff member to your institution.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                    )

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    /*
                     * Name
                     */
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = {
                            Text("Full Name")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = name.isNotEmpty() &&
                                !authViewModel.isValidName(name)
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    /*
                     * Email
                     */
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = {
                            Text("Email Address")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = email.isNotEmpty() &&
                                !authViewModel.isValidEmail(email)
                    )

                    Spacer(
                        modifier = Modifier.height(26.dp)
                    )

                    /*
                     * Buttons
                     */
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        TextButton(
                            onClick = {
                                showAddStaffDialog = false
                            }
                        ) {
                            Text("Cancel")
                        }

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Button(
                            onClick = {

                                val validName =
                                    authViewModel.isValidName(name)

                                val validEmail =
                                    authViewModel.isValidEmail(email)

                                if (validName && validEmail) {

                                    val password =
                                        generatePassword()

                                    viewModel.addStaff(
                                        name = name.trim(),
                                        email = email.trim(),
                                        password = password,
                                        instituteId = instituteId.toString()
                                    ) {

                                        Toast.makeText(
                                            context,
                                            "Staff Added",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        viewModel.getAllStaff(
                                            instituteId.toString()
                                        )
                                        showAddStaffDialog = false
                                        authViewModel.sendEmailScope(
                                            email.trim(),
                                            password
                                        ) {}
                                    }

                                } else {

                                    Toast.makeText(
                                        context,
                                        "Please fill all fields correctly",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        ) {
                            Text("Add Staff")
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