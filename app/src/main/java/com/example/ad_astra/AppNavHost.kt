package com.example.ad_astra

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(apiKey: String) {
    val navController = rememberNavController()

    androidx.compose.material3.Scaffold(
        bottomBar = { BottomNavbar(navController) }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = NavRoute.Apod.route,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {

            composable(NavRoute.Apod.route) {
                ApodScreen(apiKey = apiKey)
            }

            composable(NavRoute.Favorites.route) {
                FavoritesScreen()
            }
        }
    }
}
