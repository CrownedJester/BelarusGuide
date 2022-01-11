package com.crownedjester.soft.belarusguide.domain.use_case.get_cities

import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.data.model.CityDto
import com.crownedjester.soft.belarusguide.domain.repository.RemoteServicesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCities @Inject constructor(private val repository: RemoteServicesRepository) {

    operator fun invoke(lang: Int = 1): Flow<Resource<List<CityDto>>> = flow {
        try {
            emit(Resource.Loading())

            val result = mutableListOf<CityDto>()
            repository.getCities().forEach { city ->
                if (city.lang == lang) {
                    result.add(city)
                }
            }
            result.sortBy { city -> city.name }
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Something wrong with your connection"))
        }

    }

}