package com.crownedjester.soft.belarusguide.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceInfo(
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
) : Parcelable


