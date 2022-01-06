package com.crownedjester.soft.belarusguide.domain.use_case.get_places

import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo
import com.crownedjester.soft.belarusguide.domain.repository.RemoteServicesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPlaces @Inject constructor(
    private val repository: RemoteServicesRepository
) {

    operator fun invoke(cityId: Int, lang: Int): Flow<Resource<List<PlaceInfo>>> = flow {
        try {
            emit(Resource.Loading())

            val result = mutableListOf<PlaceInfo>()
            repository.getPlaces().forEach { place ->
                if (place.lang == lang && place.cityId == cityId) {
                    result.add(place)
                }
            }

            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Something wrong with your server connection or server itself"))
        }
    }
}