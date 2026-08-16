package com.example.pravah.nav

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pravah.R
import com.example.pravah.model.DrawerItem
import com.example.pravah.view.Staff.DeviceInfoScreen
import com.example.pravah.view.Staff.RoomInfoScreen
import com.example.pravah.view.Staff.RoomListScreen
import com.example.pravah.view.institution.ManageClassroom
import com.example.pravah.view.institution.ManageStaff
import com.example.pravah.viewmodel.ClassViewModel


private object Routes {
    const val ROOM_LIST = "room_list"
    const val MANAGE_CLASSROOM = "manage_classroom"
    const val MANAGE_STAFF = "manage_staff"

    private const val ROOM_INFO_BASE = "roomInfo"
    const val ROOM_INFO_PATTERN = "$ROOM_INFO_BASE/{roomId}"
    fun roomInfo(roomId: String) = "$ROOM_INFO_BASE/$roomId"

    private const val DEVICE_INFO_BASE = "deviceInfo"
    const val DEVICE_INFO_PATTERN = "$DEVICE_INFO_BASE/{deviceId}"
    fun deviceInfo(deviceId: String) = "$DEVICE_INFO_BASE/$deviceId"
}

@SuppressLint("CommitPrefEdits")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNavigation(rootNavController: NavController) {

    val navController = rememberNavController()
    val instituteItems = listOf(
        DrawerItem("Rooms", "room_list", Icons.Default.MeetingRoom),
        DrawerItem("Manage Classrooms", "manage_classroom", Icons.Default.Class),
        DrawerItem("Staff", "manage_staff", Icons.Default.ManageAccounts)
    )
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val userType = sharedPreferences.getString("user_type", "")
    val institutionId = sharedPreferences.getString("instituteId", "") ?: ""
    val classViewModel: ClassViewModel = viewModel()

    LaunchedEffect(institutionId) {
        if (institutionId.isNotBlank()) {
            classViewModel.getRoomsByInstitute(institutionId)
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                expanded = false
                                sharedPreferences.edit {
                                    clear()
                                }
                                rootNavController.navigate("auth") {
                                    popUpTo("user_home") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            if(userType == "admin"){
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route
                NavigationBar {
                    instituteItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(item.title)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.Black,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.ROOM_LIST,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.ROOM_LIST) {
                RoomListScreen(navController = navController, viewModel = classViewModel)
            }

            if (userType == "admin") {
                composable(Routes.MANAGE_CLASSROOM) {
                    ManageClassroom(viewModel = classViewModel)
                }
                composable(Routes.MANAGE_STAFF) {
                    ManageStaff()
                }
            }

            composable(
                route = Routes.ROOM_INFO_PATTERN,
                arguments = listOf(navArgument("roomId") { type = NavType.StringType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: return@composable
                RoomInfoScreen(roomId = roomId, navController = navController, viewModel = classViewModel)
            }
            composable(
                route = Routes.DEVICE_INFO_PATTERN,
                arguments = listOf(navArgument("deviceId") { type = NavType.StringType })
            ) { backStackEntry ->
                val deviceId = backStackEntry.arguments?.getString("deviceId") ?: return@composable
                DeviceInfoScreen(deviceId = deviceId, viewModel = classViewModel)
            }
        }
    }
}