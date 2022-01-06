package com.crownedjester.soft.belarusguide.representation.util

sealed class Screen(val route: String, val title: String) {
    object CitiesScreen : Screen("cities", "Cities of Belarus")

    object PlacesScreen : Screen("places", "Famous places of chosen city")

    object PlaceDetailScreen : Screen("place_detail", "Place Detail")

    object LanguagesScreen : Screen("languages", "Languages")
}

