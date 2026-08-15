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
        devices: List<DeviceModel>,
        onSuccess: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                val success = repo.addClassWithDevices(
                    institutionId = institutionId,
                    roomNo = roomNo,
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
}