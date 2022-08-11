package com.crownedjester.soft.belarusguide.representation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.domain.datastore.DataStoreRepository
import com.crownedjester.soft.belarusguide.domain.use_case.get_cities.GetCities
import com.crownedjester.soft.belarusguide.domain.use_case.get_places.GetPlaces
import com.crownedjester.soft.belarusguide.representation.cities.CitiesState
import com.crownedjester.soft.belarusguide.representation.places.PlacesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SharedViewModel"

@FlowPreview
@HiltViewModel
class CitiesPlacesViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val getPlacesUseCase: GetPlaces,
    private val getCitiesUseCase: GetCities
) : ViewModel() {

    private val _citiesStateFlow = MutableStateFlow(CitiesState())
    val citiesStateFlow: StateFlow<CitiesState> = _citiesStateFlow

    private val _placesStateFlow = MutableStateFlow(PlacesState())
    val placesStateFlow: StateFlow<PlacesState> = _placesStateFlow

    private val retryCitiesTrigger = RetryTrigger()
    private val retryPlacesTrigger = RetryTrigger()

    init {
        viewModelScope.launch {
            dataStoreRepository.currentLangId.collectLatest { langId ->
                getCities(langId)
                getPlaces(langId)
            }
        }
    }

    fun retryRetrieveSharedData() {
        retryCitiesTrigger.retry()
        retryPlacesTrigger.retry()
    }

    private fun getCities(lang: Int) {
        retryableFlow(retryCitiesTrigger) {
            getCitiesUseCase(lang).onEach { result ->
                when (result) {
                    is Resource.Loading -> _citiesStateFlow.emit(CitiesState(isLoading = true))

                    is Resource.Success -> _citiesStateFlow.emit(CitiesState(data = result.data))

                    is Resource.Error -> _citiesStateFlow.emit(CitiesState(error = result.message))
                }
            }
        }
    }

    private fun getPlaces(lang: Int) {
        retryableFlow(retryPlacesTrigger) {
            getPlacesUseCase(lang = lang).onEach { result ->
                when (result) {
                    is Resource.Loading -> _placesStateFlow.emit(PlacesState(isLoading = true))

                    is Resource.Success -> _placesStateFlow.emit(PlacesState(data = result.data))

                    is Resource.Error -> _placesStateFlow.emit(PlacesState(error = result.message))
                }
            }
        }.launchIn(viewModelScope)
    }
}