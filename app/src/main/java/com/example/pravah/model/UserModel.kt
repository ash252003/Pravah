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

    suspend fun addStaff(
        name: String,
        email: String,
        password: String,
        institutionId: String
    ): Boolean {
        return try {
            val user = mapOf(
                "name" to name,
                "email" to email,
                "password" to password,
                "user_type" to "staff",
                "institution_id" to institutionId
            )
            firestore
                .collection("staff")
                .document()
                .set(user)
                .await()
            true
        } catch (e: Exception) {
            Log.e(
                "Error",
                "Error adding Staff: ${e.message}"
            )
            false
        }
    }

    //Login
    suspend fun checkLogin(
        name: String,
        email: String,
        password: String,
        instituteId: String
    ): LoginResult? {

        return try {

            // Check staff
            val staffRef = firestore.collection("staff")
                .whereEqualTo("institute_id", instituteId)
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .await()

            if (!staffRef.isEmpty) {

                val document = staffRef.documents.first()

                return LoginResult(
                    userType = document.getString("user_type") ?: "staff",
                    institutionId = document.getString("institution_id") ?: ""
                )
            }

            // Check admin/institution
            val institutionRef = firestore.collection("institution")
                .whereEqualTo("institution_name", name)
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .await()

            if (!institutionRef.isEmpty) {

                val document = institutionRef.documents.first()

                return LoginResult(
                    userType = document.getString("user_type") ?: "admin",
                    institutionId = document.id
                )
            }

            null

        } catch (e: Exception) {

            Log.e(
                "Login",
                "Error checking login",
                e
            )

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
    ): List<StaffDetails> {
        return try {
            val querySnapshot = firestore
                .collection("staff")
                .whereEqualTo("institute_id", institutionId)
                .get()
                .await()
            querySnapshot.documents.map { document ->
                StaffDetails(
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
                .whereEqualTo("institute_id", institutionId)
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