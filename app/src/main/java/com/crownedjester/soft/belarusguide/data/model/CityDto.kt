package com.crownedjester.soft.belarusguide.data.model

import com.google.gson.annotations.SerializedName

data class CityDto(
    val id_locale: Int,
    val id: Int,
    val name: String,
    val lang: Int,
    val logo: String,
    val visible: Boolean,
    @SerializedName("city_is_regional")
    val cityIsRegional: Boolean,
    val region: String
)
