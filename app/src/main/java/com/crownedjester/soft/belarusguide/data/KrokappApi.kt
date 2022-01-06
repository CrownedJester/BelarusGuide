package com.crownedjester.soft.belarusguide.data

import com.crownedjester.soft.belarusguide.data.model.CityDto
import com.crownedjester.soft.belarusguide.data.model.LanguageDto
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo
import retrofit2.http.GET

interface KrokappApi {

    @GET("api/get_cities/11")
    suspend fun getCities(): List<CityDto>

    @GET("api/get_points/11")
    suspend fun getPoints(): List<PlaceInfo>

    @GET("api/get_languages")
    suspend fun getLanguages(): List<LanguageDto>
}