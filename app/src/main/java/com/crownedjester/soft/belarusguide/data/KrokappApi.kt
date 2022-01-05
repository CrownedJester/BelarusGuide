package com.crownedjester.soft.belarusguide.data

import com.crownedjester.soft.belarusguide.data.model.CityDto
import com.crownedjester.soft.belarusguide.data.model.LanguageDto
import com.crownedjester.soft.belarusguide.data.model.PointInfo
import retrofit2.http.GET

interface KrokappApi {

    @GET("api/get_cities/11")
    suspend fun getCities(): List<CityDto>

    @GET("api/get_points/11")
    fun getPoints(): List<PointInfo>

    @GET("api/get_languages")
    fun getLanguages(): List<LanguageDto>
}