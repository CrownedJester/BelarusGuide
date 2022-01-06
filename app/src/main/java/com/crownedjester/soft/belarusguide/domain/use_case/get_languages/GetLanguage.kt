package com.crownedjester.soft.belarusguide.domain.use_case.get_languages

import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.data.model.LanguageDto
import com.crownedjester.soft.belarusguide.domain.repository.RemoteServicesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetLanguage @Inject constructor(
    private val repository: RemoteServicesRepository
) {

    operator fun invoke(): Flow<Resource<List<LanguageDto>>> = flow {
        try {
            emit(Resource.Loading())

            val result = repository.getLanguages()

            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Something wrong with your connection to server or server itself"))
        }
    }
}