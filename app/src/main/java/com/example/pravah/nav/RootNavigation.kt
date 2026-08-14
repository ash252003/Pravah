package com.example.pravah.nav

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun RootNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        composable("auth") {
            AppNavigation(navController)
        }
        composable("user_home") {
            UserNavigation(navController)
        }
    }
}