package com.example.pravah.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SplashViewModel(): ViewModel() {

    var isLoading = mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            delay(2000.milliseconds) // Simulate a network call or heavy initialization
            isLoading.value = false
        }
    }
}