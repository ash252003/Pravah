package com.example.pravah.model

data class DataModel(
    val id: String,
    val institution: String
)
data class RoomModel(
    val id: String = "",
    val institutionId: String = "",
    val roomNo: String = "",
    val devices: List<DeviceModel> = emptyList(),
    val status: String = "working"
)

data class DeviceModel(
    val deviceName: String = "",
    val status: String = "working",
    val powerStatus: String? = null
)
