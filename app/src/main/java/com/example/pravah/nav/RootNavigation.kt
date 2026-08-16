package com.example.pravah.nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RootNavigation() {

    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences(
        "user_session",
        Context.MODE_PRIVATE
    )

    val isLoggedIn = sharedPreferences.getBoolean(
        "isLoggedIn",
        false
    )

    val navController = rememberNavController()

    NavHost(
        navController = navController,

        startDestination = if (isLoggedIn) {
            "user_home"
        } else {
            "auth"
        }
    ) {

        composable("auth") {
            AppNavigation(navController)
        }

        composable("user_home") {
            UserNavigation(navController)
        }
    }
}