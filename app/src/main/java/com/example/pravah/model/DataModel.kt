package com.example.pravah.model

import androidx.compose.ui.graphics.vector.ImageVector

data class DataModel(
    val id: String,
    val institution: String
)

data class DrawerItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)
