package com.crownedjester.soft.belarusguide.domain.repository

import com.crownedjester.soft.belarusguide.data.KrokappApi
import com.crownedjester.soft.belarusguide.data.model.CityDto
import com.crownedjester.soft.belarusguide.data.model.LanguageDto
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo
import javax.inject.Inject

class RemoteServicesRepositoryImpl @Inject constructor(
    private val remoteApiServices: KrokappApi
) : RemoteServicesRepository {

    override suspend fun getCities(): List<CityDto> =
        remoteApiServices.getCities()

    override suspend fun getPlaces(): List<PlaceInfo> =
        remoteApiServices.getPoints()

    override suspend fun getLanguages(): List<LanguageDto> =
        remoteApiServices.getLanguages()
}