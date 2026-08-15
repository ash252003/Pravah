package com.example.pravah.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pravah.model.UserModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.pravah.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

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

    suspend fun sendEmail(toEmail: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val props = Properties().apply {
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.port", "587")
                }

                val session = Session.getInstance(props, object : javax.mail.Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(
                            BuildConfig.GMAIL_ADDRESS,
                            BuildConfig.GMAIL_APP_PASSWORD
                        )
                    }
                })
                val json = """
                {
                    "email": "$toEmail",
                    "subject": "Password",
                    "message": "Your Password for Pravah is: $password"
                }
                """.trimIndent()

                val body = json.toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("https://courtinsight-email-api.onrender.com/send-email")
                    .post(body)
                    .build()

                val client = OkHttpClient()
                val response = client.newCall(request).execute()

                response.body?.string()

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(BuildConfig.GMAIL_ADDRESS))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                    subject = "Password"
                    setText("Your Password for Pravah is: $password")
                }

                Transport.send(message)
                true
            } catch (e: Exception) {
                Log.e("Error", "Direct email send failed: ${e.message}")
                Log.d("DebugAuth", "Using email: ${BuildConfig.GMAIL_ADDRESS}, pass length: ${BuildConfig.GMAIL_APP_PASSWORD.length}")
                false
            }
        }
    }

    fun sendEmailScope(email: String, password: String, onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            isPasswordLoading = true
            val success = sendEmail(email, password)
            isPasswordLoading = false
            onResult(success)
        }
    }
}