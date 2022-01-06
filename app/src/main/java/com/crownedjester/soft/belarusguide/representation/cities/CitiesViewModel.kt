package com.crownedjester.soft.belarusguide.representation.cities

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.domain.use_case.get_cities.GetCities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCitiesUseCase: GetCities
) : ViewModel() {

    private val _dataState = mutableStateOf(CitiesState())
    val dataState: State<CitiesState> = _dataState

    init {
        getCities()
    }

    private fun getCities(lang: Int = 1) {
        getCitiesUseCase(lang).onEach { result ->
            when (result) {
                is Resource.Loading ->
                    _dataState.value = CitiesState(isLoading = true)

                is Resource.Success ->
                    _dataState.value = CitiesState(data = result.data)

                is Resource.Error ->
                    _dataState.value = CitiesState(error = result.message)
            }
        }.launchIn(viewModelScope)
    }
}