package com.example.rusticroots.pages

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.MainScreen.route
    ) {

        composable(route = BottomBarScreen.CartScreen.route) {
            CartScreen()
        }
        composable(route = BottomBarScreen.BookingScreen.route) {
            BookingScreen()
        }
        composable(route = BottomBarScreen.MainScreen.route) {
            HomeScreen()
        }
        composable(route = BottomBarScreen.ProfileScreen.route) {
            ProfileScreen()
        }
        composable(route = BottomBarScreen.SettingScreen.route) {
            SettingsScreen()
        }
    }
}