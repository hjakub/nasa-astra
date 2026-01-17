package com.example.ad_astra

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavbar(navController: NavController) {

    val items = listOf(
        NavRoute.Apod to Icons.Filled.Home,
        NavRoute.Favorites to Icons.Filled.Favorite
    )

    NavigationBar {
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry.value?.destination?.route

        items.forEach { (route, icon) ->
            NavigationBarItem(
                selected = currentRoute == route.route,
                onClick = {
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = route.route) },
                label = { Text(route.route.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}
