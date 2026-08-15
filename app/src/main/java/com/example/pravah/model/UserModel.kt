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
    suspend fun getRoomsByInstitution(
        institutionId: String
    ): List<RoomModel> {

        return try {

            val querySnapshot = firestore
                .collection("room")
                .whereEqualTo("institution_id", institutionId)
                .get()
                .await()

            querySnapshot.documents.map { document ->

                val devices = document
                    .get("devices") as? List<Map<String, Any>>
                    ?: emptyList()

                val deviceList = devices.map { device ->

                    DeviceModel(
                        deviceName = device["deviceName"] as? String ?: "",
                        status = device["status"] as? String ?: "working",
                        powerStatus = device["powerStatus"] as? String
                    )
                }

                RoomModel(
                    id = document.id,
                    institutionId = document.getString("institution_id") ?: "",
                    roomNo = document.getString("room_no") ?: "",
                    devices = deviceList,
                    status = document.getString("status") ?: "working"
                )
            }

        } catch (e: Exception) {

            Log.e(
                "Room",
                "Error getting rooms: ${e.message}",
                e
            )

            emptyList()
        }
    }

    suspend fun getAllStaff(institutionName: String): List<StaffDetails>{
        val staffDetail = mutableListOf<StaffDetails>()
        try {
            val collectionRef = firestore.collection("staff")
            val querySnapshot = collectionRef.get().await()
            for(document in querySnapshot.documents){
                val id = document.id
                val name = document.getString("name") ?: ""
                val email = document.getString("email") ?: ""
                val staff = StaffDetails(id, name, email)
                staffDetail.add(staff)
            }
            return staffDetail
        } catch (e: Exception){
            Log.e("Error", "Error Getting Institution: ${e.message}")
            return emptyList()
        }
    }

    suspend fun deleteStaff(email: String): Boolean {
        return try {
            val query = firestore.collection("staff")
                .whereEqualTo("email", email)
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
}