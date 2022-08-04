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

    operator fun invoke(lang: Int): Flow<Resource<List<CityDto>>> = flow {
        try {
            emit(Resource.Loading())

            emit(
                Resource.Success(
                    repository.getCities()
                        .filter { it.lang == lang }
                        .sortedBy { it.id }
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Something wrong with your connection"))
        }

    }

}