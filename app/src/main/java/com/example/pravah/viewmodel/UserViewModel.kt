package com.example.pravah.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pravah.model.DataModel
import com.example.pravah.model.RoomModel
import com.example.pravah.model.UserModel
import com.example.pravah.model.staffDetails
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
    var staff by mutableStateOf<List<staffDetails>>(emptyList())
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
    fun getRoomsByStaff(institutionId: String) {

        viewModelScope.launch {

            isLoading = true

            rooms = repo.getRoomsByInstitution(institutionId)

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

    fun getAllStaff(institutionName: String) {
        viewModelScope.launch {
            isLoading = true
            staff = repo.getAllStaff(institutionName)
            isLoading = false
        }
    }
}