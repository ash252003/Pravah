package com.example.pravah.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class UserModel {
    val firestore = FirebaseFirestore.getInstance()

    suspend fun addInstitution(name: String, email: String, password: String): Boolean{
        try {
            val user = mapOf(
                "institution_name" to name,
                "email" to email,
                "password" to password,
                "user_type" to "admin"
            )
            val collectionRef = firestore.collection("institution")
                .document()
            collectionRef.set(user).await()
            return true
        } catch (e: Exception){
            Log.e("Error", "Error adding User: ${e.message}")
            return false
        }
    }

    suspend fun addStaff(name: String, email: String, password: String): Boolean{
        try {
            val user = mapOf(
                "name" to name,
                "email" to email,
                "password" to password,
                "user_type" to "staff"
            )
            val collectionRef = firestore.collection("staff")
                .document()
            collectionRef.set(user).await()
            return true
        } catch (e: Exception){
            Log.e("Error", "Error adding User: ${e.message}")
            return false
        }
    }

    //Login
    suspend fun checkLogin(
        name: String,
        email: String,
        password: String
    ): String? {
        return try {
            val staffRef = firestore.collection("staff")
                .whereEqualTo("institution_name", name)
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .await()

            if (!staffRef.isEmpty) {
                return staffRef.documents.firstOrNull()?.getString("user_type")
            }

            val institutionRef = firestore.collection("institution")
                .whereEqualTo("institution_name", name)
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .await()

            institutionRef.documents.firstOrNull()?.getString("user_type")
        } catch (e: Exception) {
            Log.e("Login", "Error checking login", e)
            null
        }
    }

    suspend fun checkEmail(email: String): Boolean{
        try {
            val userRef = firestore.collection("institution")
                .whereEqualTo("email", email)
                .get()
                .await()
            if(!userRef.isEmpty){
                return true
            }
        } catch (e: Exception){
            Log.e("Error", "Error checking email: ${e.message}")
        }
        return false
    }
}