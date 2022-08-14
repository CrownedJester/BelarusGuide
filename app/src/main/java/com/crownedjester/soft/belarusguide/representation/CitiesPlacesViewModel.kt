package com.crownedjester.soft.belarusguide.representation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.data.model.CityDto
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo
import com.crownedjester.soft.belarusguide.domain.datastore.DataStoreRepository
import com.crownedjester.soft.belarusguide.domain.use_case.get_cities.GetCities
import com.crownedjester.soft.belarusguide.domain.use_case.get_places.GetPlaces
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

    private val _citiesStateFlow = MutableStateFlow(UiDataState<CityDto>())
    val citiesStateFlow: StateFlow<UiDataState<CityDto>> = _citiesStateFlow

    private val _placesStateFlow = MutableStateFlow(UiDataState<PlaceInfo>())
    val placesStateFlow: StateFlow<UiDataState<PlaceInfo>> = _placesStateFlow

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
                    is Resource.Loading -> _citiesStateFlow.emit(UiDataState(isLoading = true))

                    is Resource.Success -> _citiesStateFlow.emit(UiDataState(data = result.data))

                    is Resource.Error -> _citiesStateFlow.emit(UiDataState(error = result.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getPlaces(lang: Int) {
        retryableFlow(retryPlacesTrigger) {
            getPlacesUseCase(lang = lang).onEach { result ->
                when (result) {
                    is Resource.Loading -> _placesStateFlow.emit(UiDataState(isLoading = true))

                    is Resource.Success -> _placesStateFlow.emit(UiDataState(data = result.data))

                    is Resource.Error -> _placesStateFlow.emit(UiDataState(error = result.message))
                }
            }
        }.launchIn(viewModelScope)
    }

}