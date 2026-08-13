package com.example.pravah.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel

class AuthViewModel(): ViewModel() {
    fun isValidEmail(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidPassword(password: String): Boolean {
        return password.length >= 8 && password.any { it.isUpperCase() } && password.any { it.isDigit() }
    }
    fun isValidName(name: String): Boolean {
        return name.length >= 3
    }
    fun isValidConfirmPassword(password: String, confirmPassword: String): Boolean {
        return confirmPassword.isNotEmpty() && confirmPassword.equals(password)
    }
}