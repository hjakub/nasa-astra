package com.example.ad_astra

sealed class NavRoute(val route: String) {
    object Apod : NavRoute("Picture of the Day")
    object Favorites : NavRoute("Favorites")
}
