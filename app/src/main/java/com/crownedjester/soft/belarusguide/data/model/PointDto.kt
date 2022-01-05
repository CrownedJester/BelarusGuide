package com.crownedjester.soft.belarusguide.data.model

import com.crownedjester.soft.belarusguide.domain.model.Point
import com.google.gson.annotations.SerializedName

data class PointDto(
    val city: City,
    val id: Int,
    val lat: Double,
    val lng: Double,
    @SerializedName("point_images")
    val images: List<PointImage>,
    @SerializedName("point_key_name")
    val name: String,
    val url: String
)

fun PointDto.toPoint(): Point {
    val imagesList = mutableListOf<String>()
    images.forEach { image ->
        imagesList.add(image.file)
    }

    return Point(
        id = id,
        lat = lat,
        lng = lng,
        images = imagesList,
        name = name,
        url = url,
        cityId = city.id
    )
}