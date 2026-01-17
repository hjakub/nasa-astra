package com.example.ad_astra

sealed class NavRoute(val route: String) {
    object Apod : NavRoute("apod")
    object Favorites : NavRoute("favorites")
}
