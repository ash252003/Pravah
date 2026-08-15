package com.example.pravah.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pravah.model.ClassroomModel
import com.example.pravah.model.DeviceModel
import com.example.pravah.model.RoomModel
import com.example.pravah.model.UserModel
import kotlinx.coroutines.launch

class ClassViewModel(private val repo: ClassroomModel = ClassroomModel()): ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var rooms by mutableStateOf<List<RoomModel>>(emptyList())
        private set

    fun getRoomsByInstitute(institutionId: String) {
        viewModelScope.launch {
            isLoading = true
            rooms = repo.getRoomsByInstitution(institutionId)
            isLoading = false
        }
    }

    fun addRoom(
        institutionId: String,
        roomNo: String,
        roomStatus: String,
        devices: List<DeviceModel>,
        onSuccess: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                val success = repo.addClassWithDevices(
                    institutionId = institutionId,
                    roomNo = roomNo,
                    roomStatus = roomStatus,
                    devices = devices
                )
                onSuccess(success)
            } catch (e: Exception) {
                Log.e(
                    "Room",
                    "Error adding room: ${e.message}",
                    e
                )
                onSuccess(false)
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteRoom(
        roomId: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true

                val success = repo.deleteRoom(roomId)

                if (success) {
                    Log.d("Room", "Room deleted successfully")
                } else {
                    Log.e("Room", "Failed to delete room")
                }

                onResult(success)

            } catch (e: Exception) {

                Log.e(
                    "Room",
                    "Error deleting room: ${e.message}",
                    e
                )

                onResult(false)

            } finally {
                isLoading = false
            }
        }
    }

    fun editRoom(
        roomId: String,
        roomNo: String,
        devices: List<DeviceModel>,
        institutionId: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {

            try {

                isLoading = true

                val success = repo.editRoom(
                    roomId = roomId,
                    roomNo = roomNo,
                    devices = devices
                )

                if (success) {

                    getRoomsByInstitute(institutionId)
                }

                onResult(success)

            } catch (e: Exception) {

                Log.e(
                    "Room",
                    "Error editing room",
                    e
                )

                onResult(false)

            } finally {

                isLoading = false
            }
        }
    }
    fun toggleDevicePower(deviceId: String, isOn: Boolean) {
        viewModelScope.launch {
            val newStatus = if (isOn) "ON" else "OFF"
            val success = repo.updateDevicePowerStatus(deviceId, newStatus)

            if (success) {
                rooms = rooms.map { room ->
                    room.copy(
                        devices = room.devices.map { device ->
                            if (device.id == deviceId) device.copy(powerStatus = newStatus) else device
                        }
                    )
                }
            } else {
                Log.e("Device", "Failed to update power status for device $deviceId")
            }
        }
    }
}