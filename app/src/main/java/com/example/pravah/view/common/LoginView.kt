package com.example.pravah.view.common

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pravah.R
import com.example.pravah.viewmodel.AuthViewModel
import com.example.pravah.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(navController: NavController, rootNavController: NavController, viewModel: AuthViewModel = viewModel(),
              userViewModel: UserViewModel = viewModel()){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect{ msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
    if (isLoggedIn){
        rootNavController.navigate("user_home") {
            popUpTo("login") { inclusive = true }
        }
    }
    LaunchedEffect(Unit) {
        userViewModel.getAllInstitution()
    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedInstitution by remember { mutableStateOf("") }
    val institution = userViewModel.institute
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.pravah_light),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedInstitution,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("Select Institution")
                            },
                            placeholder = {
                                Text("Choose Institution")
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            institution.forEach { institution ->

                                DropdownMenuItem(
                                    text = {
                                        Text(institution.institution)
                                    },
                                    onClick = {
                                        selectedInstitution = institution.institution
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = {email = it},
                        label = { Text(stringResource(R.string.email)) },
                        singleLine = true,
                        isError = email.isNotEmpty() && !viewModel.isValidEmail(email),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {password = it},
                        label = { Text(stringResource(R.string.password)) },
                        singleLine = true,
                        isError = password.isNotEmpty() && !viewModel.isValidPassword(password),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Password,
                                contentDescription = stringResource(R.string.password),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            val image = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = stringResource(R.string.password),
                                    tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(R.string.forgot_password),
                            color = colorResource(R.color.purple_700),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable {

                                },
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if(viewModel.isValidEmail(email) && viewModel.isValidPassword(password)){
                                viewModel.checkLogin(selectedInstitution, email, password){ userType ->
                                    userViewModel.getInstitutionId(selectedInstitution){ id ->
                                        saveLogin(context, userType.toString(), selectedInstitution, email, id)
                                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                                        rootNavController.navigate("user_home"){
                                            popUpTo("auth"){inclusive = true}
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = Shapes().medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = colorResource(R.color.white)
                        )
                    ) {
                        Text(stringResource(R.string.login))
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) { Text(text = stringResource(R.string.new_user),
                        color = colorResource(R.color.black),
                        modifier = Modifier.padding(end = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                        Text(text = stringResource(R.string.register),
                            color = colorResource(R.color.purple_700),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable{navController.navigate("register")}
                        )
                    }
                }
            }
        }
    }
}

fun saveLogin(context: Context, userType: String, name: String, email: String, id: String){
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putBoolean("isLoggedIn", true)
        putString("user_type", userType)
        putString("name", name)
        putString("email", email)
        putString("instituteId", id)
        apply()
    }
}

@Composable
@Preview(showSystemUi = true)
fun LoginPreview(){
    val navController = rememberNavController()
    LoginView(navController, navController)
}
