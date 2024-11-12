package com.example.cinemaapp.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val id: String,
    val icon: ImageVector,
    val title: String,
)

val drawerItems = listOf(
    DrawerItem(
        id = "profile",
        icon = Icons.Default.Person,
        title = "Trang cá nhân"
    ),
    DrawerItem(
        id = "search",
        icon = Icons.Default.Search,
        title = "Tìm kiếm"
    ),
    DrawerItem(
        id = "settings",
        icon = Icons.Default.Settings,
        title = "Cài đặt"
    )

)
