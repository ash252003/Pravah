package com.example.pravah.model

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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

    suspend fun addStaff(name: String, email: String, password: String, instituteName: String): Boolean{
        try {
            val user = mapOf(
                "name" to name,
                "email" to email,
                "password" to password,
                "institution_name" to instituteName,
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

    suspend fun getAllInstitution(): List<DataModel>{
        val institution = mutableListOf<DataModel>()
        try {
            val collectionRef = firestore.collection("institution")
            val querySnapshot = collectionRef.get().await()
            for(document in querySnapshot.documents){
                val id = document.id
                val name = document.getString("institution_name") ?: ""
                val institute = DataModel(id, name)
                institution.add(institute)
            }
            return institution
        } catch (e: Exception){
            Log.e("Error", "Error Getting Institution: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getAllStaff(
        institutionId: String
    ): List<staffDetails> {
        return try {
            val querySnapshot = firestore
                .collection("staff")
                .whereEqualTo("institution_id", institutionId)
                .get()
                .await()
            querySnapshot.documents.map { document ->
                staffDetails(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    email = document.getString("email") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e(
                "Staff",
                "Error getting staff: ${e.message}",
                e
            )
            emptyList()
        }
    }

    suspend fun deleteStaff(email: String, institutionId: String): Boolean {
        return try {
            val query = firestore.collection("staff")
                .whereEqualTo("email", email)
                .whereEqualTo("institution_id", institutionId)
                .get()
                .await()
            if (!query.isEmpty) {
                query.documents.forEach { document ->
                    document.reference.delete().await()
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error deleting staff: ${e.message}")
            false
        }
    }

    suspend fun getInstitutionIdByName(instituteName: String): String? {
        return try {
            val querySnapshot = firestore
                .collection("institution")
                .whereEqualTo("institution_name", instituteName)
                .limit(1)
                .get()
                .await()
            val document = querySnapshot.documents.firstOrNull()
            document?.id
        } catch (e: Exception) {
            Log.e(
                "Firestore",
                "Error Getting InstitutionId: ${e.message}",
                e
            )
            null
        }
    }
}