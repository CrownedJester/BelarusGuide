package com.crownedjester.soft.belarusguide.data.model

import com.google.gson.annotations.SerializedName

data class PointInfo(
    val id: Int,
    @SerializedName("id_point")
    val idPoint: Int,
    val name: String,
    val text: String,
    val sound: String,
    val lang: Int,
    val lat: Double,
    val lng: Double,
    val logo: String,
    val photo: String,
    @SerializedName("city_id")
    val cityId: Int,
    val images: List<String>,
    @SerializedName("last_edit_time")
    val lastEditTime: Long
)


