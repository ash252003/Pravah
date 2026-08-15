package com.example.pravah.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClassroomModel {
    val firestore = FirebaseFirestore.getInstance()

    suspend fun addClassWithDevices(
        institutionId: String,
        roomNo: String,
        roomStatus: String = "empty",
        devices: List<DeviceModel>
    ): Boolean {
        return try {
            val batch = firestore.batch()
            // Create classroom document
            val classroomRef = firestore
                .collection("classroom")
                .document()
            val classroomData = hashMapOf(
                "institution_id" to institutionId,
                "roomNo" to roomNo,
                "room_status" to roomStatus
            )
            batch.set(classroomRef, classroomData)
            // Create devices
            for (device in devices) {
                val deviceRef = firestore
                    .collection("devices")
                    .document()
                val deviceData = hashMapOf(
                    "classId" to classroomRef.id,
                    "espId" to device.deviceName,
                    "status" to device.status,
                    "powerStatus" to device.powerStatus
                )
                batch.set(deviceRef, deviceData)
            }
            // Commit classroom + all devices together
            batch.commit().await()
            true
        } catch (e: Exception) {
            Log.e(
                "Firestore",
                "Error adding classroom and devices",
                e
            )
            false
        }
    }

    suspend fun getRoomsByInstitution(
        institutionId: String
    ): List<RoomModel> {
        return try {
            val roomSnapshot = firestore
                .collection("classroom")
                .whereEqualTo("institution_id", institutionId)
                .get()
                .await()

            roomSnapshot.documents.map { document ->
                val classId = document.id
                // Get devices belonging to this classroom
                val deviceSnapshot = firestore
                    .collection("devices")
                    .whereEqualTo("classId", classId)
                    .get()
                    .await()

                val deviceList = deviceSnapshot.documents.map { deviceDocument ->
                    DeviceModel(
                        id = deviceDocument.id,
                        classId = classId,
                        deviceName = deviceDocument.getString("espId") ?: "",
                        status = deviceDocument.getString("status") ?: "working",
                        powerStatus = deviceDocument.getString("powerStatus")
                    )
                }
                RoomModel(
                    id = classId,
                    institutionId = document.getString("institute_id") ?: "",
                    roomNo = document.getString("roomNo") ?: "",
                    devices = deviceList,
                    status = document.getString("room_status") ?: "active"
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

    suspend fun deleteRoom(roomId: String): Boolean {
        return try {

            val batch = firestore.batch()

            // Delete classroom
            val roomRef = firestore
                .collection("classroom")
                .document(roomId)

            batch.delete(roomRef)

            // Find all devices belonging to this classroom
            val deviceSnapshot = firestore
                .collection("devices")
                .whereEqualTo("classId", roomId)
                .get()
                .await()

            // Delete all devices
            for (document in deviceSnapshot.documents) {
                batch.delete(document.reference)
            }

            // Commit all deletions
            batch.commit().await()

            true

        } catch (e: Exception) {

            Log.e(
                "Firestore",
                "Error deleting room: ${e.message}",
                e
            )

            false
        }
    }

    suspend fun editRoom(
        roomId: String,
        roomNo: String,
        devices: List<DeviceModel>
    ): Boolean {

        return try {

            val batch = firestore.batch()

            // Update classroom
            val roomRef = firestore
                .collection("classroom")
                .document(roomId)

            batch.update(
                roomRef,
                "roomNo",
                roomNo
            )

            // Get existing devices
            val deviceSnapshot = firestore
                .collection("devices")
                .whereEqualTo("classId", roomId)
                .get()
                .await()
            // Delete existing devices
            for (document in deviceSnapshot.documents) {
                batch.delete(document.reference)
            }
            // Add updated devices
            for (device in devices) {

                val deviceRef = firestore
                    .collection("devices")
                    .document()

                val deviceData = hashMapOf(
                    "classId" to roomId,
                    "espId" to device.deviceName,
                    "status" to device.status,
                    "powerStatus" to device.powerStatus
                )

                batch.set(
                    deviceRef,
                    deviceData
                )
            }
            batch.commit().await()
            true
        } catch (e: Exception) {
            Log.e(
                "Firestore",
                "Error editing classroom",
                e
            )
            false
        }
    }
}