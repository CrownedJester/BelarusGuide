package com.crownedjester.soft.belarusguide.domain.repository

import com.crownedjester.soft.belarusguide.data.model.CityDto
import com.crownedjester.soft.belarusguide.data.model.LanguageDto
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo

interface RemoteServicesRepository {

    suspend fun getCities(): List<CityDto>

    suspend fun getPlaces(): List<PlaceInfo>

    suspend fun getLanguages(): List<LanguageDto>
}
