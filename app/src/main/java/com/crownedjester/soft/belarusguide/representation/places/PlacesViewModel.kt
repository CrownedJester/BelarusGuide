package com.crownedjester.soft.belarusguide.representation.places

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.domain.use_case.get_places.GetPlaces
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val getPlacesUseCase: GetPlaces
) : ViewModel() {

    private val _placesState = mutableStateOf(PlacesState())
    val placesState: State<PlacesState> = _placesState

    init {
        getPlaces(cityId = 1, lang = 1)
    }

    private fun getPlaces(cityId: Int = 1, lang: Int = 1) {
        getPlacesUseCase(cityId = cityId, lang = lang).onEach { result ->
            when (result) {
                is Resource.Loading ->
                    _placesState.value = PlacesState(isLoading = true)
                is Resource.Success ->
                    _placesState.value = PlacesState(data = result.data)
                is Resource.Error ->
                    _placesState.value = PlacesState(error = result.message)
            }
        }.launchIn(viewModelScope)
    }
}