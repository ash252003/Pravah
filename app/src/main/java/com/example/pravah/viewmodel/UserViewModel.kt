package com.example.pravah.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pravah.model.DataModel
import com.example.pravah.model.RoomModel
import com.example.pravah.model.UserModel
import com.example.pravah.model.StaffDetails
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserModel = UserModel()): ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()
    var isStaffLoading by mutableStateOf(false)
        private set
    var institute by mutableStateOf<List<DataModel>>(emptyList())
        private set
    var staff by mutableStateOf<List<StaffDetails>>(emptyList())
        private set
    var rooms by mutableStateOf<List<RoomModel>>(emptyList())
        private set
    fun getAllInstitution() {
        viewModelScope.launch {
            isLoading = true
            institute = repo.getAllInstitution()
            isLoading = false
        }
    }

    fun addStaff(
        name: String,
        email: String,
        password: String,
        instituteName: String,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {
            isStaffLoading = true
            val result = repo.addStaff(name, email, password, instituteName)
            isStaffLoading = false
            if(result){
                onSuccess()
            } else {
                _toastMessage.emit("Failed to Add")
            }
        }
    }

    fun getAllStaff(institutionId: String) {
        viewModelScope.launch {
            isLoading = true
            staff = repo.getAllStaff(institutionId)
            isLoading = false
        }
    }

    fun deleteStaff(email: String, institutionId: String, onSuccess: () -> Unit){
        viewModelScope.launch {
            val success = repo.deleteStaff(email, institutionId)
            if (success){
                onSuccess()
            } else {
                _toastMessage.emit("Failed to Delete")
            }
        }
    }

    fun getInstitutionId(
        instituteName: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val institutionId = repo.getInstitutionIdByName(instituteName)
            if (institutionId != null) {
                onSuccess(institutionId)
            } else {
                Log.e("Institution", "Institution not found")
            }
        }
    }
}