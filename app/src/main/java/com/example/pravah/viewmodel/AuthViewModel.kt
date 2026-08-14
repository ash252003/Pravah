package com.example.pravah.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pravah.model.UserModel
import com.example.pravah.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class AuthViewModel(private val repo: UserModel = UserModel()): ViewModel() {

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()
    var isRegistrationLoading by mutableStateOf(false)
        private set

    var isLoginLoading by mutableStateOf(false)
        private set
    var isPasswordLoading by mutableStateOf(false)
        private set
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

    fun addInstitution(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {
            isRegistrationLoading = true
            val result = repo.addInstitution(name, email, password)
            isRegistrationLoading = false
            if(result){
                onSuccess()
            } else {
                _toastMessage.emit("Registration Failed")
            }
        }
    }

    fun checkLogin(
        name: String,
        email: String,
        password: String,
        onSuccess: (String?) -> Unit
    ){
        viewModelScope.launch {
            isLoginLoading = true
            val result = repo.checkLogin(name, email, password)
            isLoginLoading = false
            if(result != null){
                _toastMessage.emit("Login Successful")
                onSuccess(result)
            }else{
                _toastMessage.emit("Login Failed")
            }
        }
    }

    fun checkEmail(email: String, onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            onResult(repo.checkEmail(email))
        }
    }

    suspend fun sendEmail(email: String, password: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val json = """
        {
            "to": "$email",
            "subject": "Password",
            "message": "Your Password for Pravah is: $password"
        }
        """.trimIndent()

                val body = json.toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("https://courtinsight-email-api.onrender.com/send-email")
                    .addHeader("X-API-KEY", BuildConfig.MAILER_API_KEY)
                    .post(body)
                    .build()

                val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    Log.e("Error", "Email send failed: ${response.code} ${response.body?.string()}")
                    return@withContext null
                }

                response.body?.string()

            } catch (e: Exception) {
                Log.e("Error", "Error sending email: ${e.message}")
                null
            }
        }
    }

    fun sendEmailScope(email: String, password: String, onResult: (String?) -> Unit){
        viewModelScope.launch {
            isPasswordLoading = true
            val pass = sendEmail(email, password)
            isPasswordLoading = false
            onResult(pass)
        }
    }
}