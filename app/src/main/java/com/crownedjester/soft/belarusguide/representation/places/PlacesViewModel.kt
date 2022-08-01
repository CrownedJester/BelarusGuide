package com.crownedjester.soft.belarusguide.representation.places

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.domain.datastore.DataStoreRepository
import com.crownedjester.soft.belarusguide.domain.use_case.get_places.GetPlaces
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val getPlacesUseCase: GetPlaces,
    savedStateHandle: SavedStateHandle,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _placesState = mutableStateOf(PlacesState())
    val placesState: State<PlacesState> = _placesState

    init {
        savedStateHandle.get<Int>("cityId")?.let { cityId ->
            viewModelScope.launch {
                dataStoreRepository.currentLangId.collectLatest { langId ->
                    getPlaces(cityId = cityId, lang = langId)
                }
            }
        }
    }

    private fun getPlaces(cityId: Int, lang: Int) {
        getPlacesUseCase(cityId = cityId, lang = lang).onEach { result ->
            when (result) {
                is Resource.Loading ->
                    _placesState.value = PlacesState(isLoading = true)
                is Resource.Success -> {
                    _placesState.value = PlacesState(data = result.data)
                    Log.i("PlacesViewModel", result.data.toString())
                }
                is Resource.Error ->
                    _placesState.value = PlacesState(error = result.message)
            }
        }.launchIn(viewModelScope)
    }
}