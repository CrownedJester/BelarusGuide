package com.crownedjester.soft.belarusguide.domain.model

data class Point(
    val id: Int,
    val lat: Double,
    val lng: Double,
    val images: List<String>,
    val name: String,
    val url: String,
    val cityId: Int
)
