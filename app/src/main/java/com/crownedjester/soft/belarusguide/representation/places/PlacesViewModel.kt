package com.crownedjester.soft.belarusguide.representation.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.data.model.PlaceInfo
import com.crownedjester.soft.belarusguide.domain.use_case.get_places.GetPlaces
import com.crownedjester.soft.belarusguide.representation.RetryTrigger
import com.crownedjester.soft.belarusguide.representation.UiDataState
import com.crownedjester.soft.belarusguide.representation.retryableFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val getPlacesUseCase: GetPlaces
) : ViewModel() {

    private val _placesStateFlow = MutableStateFlow(UiDataState<PlaceInfo>())
    val placesStateFlow: StateFlow<UiDataState<PlaceInfo>> = _placesStateFlow

    private val retryPlacesTrigger = RetryTrigger()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaces()
        }
    }


    fun retryRetrievePlaces() {
        retryPlacesTrigger.retry()
    }

    private fun getPlaces() {
        retryableFlow(retryPlacesTrigger) {
            getPlacesUseCase().onEach { result ->
                when (result) {
                    is Resource.Loading -> _placesStateFlow.emit(UiDataState(isLoading = true))

                    is Resource.Success -> _placesStateFlow.emit(UiDataState(data = result.data))

                    is Resource.Error -> _placesStateFlow.emit(UiDataState(error = result.message))
                }
            }
        }.launchIn(viewModelScope)
    }

}