package com.example.rusticroots.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object CartScreen : BottomBarScreen(
        route = "Cart",
        title = "Cart",
        icon = Icons.Default.ShoppingCart)

    object BookingScreen : BottomBarScreen(
        route = "Booking",
        title = "Book",
        icon = Icons.Default.List)

    object MainScreen : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object ProfileScreen : BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    object SettingScreen : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}