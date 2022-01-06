package com.crownedjester.soft.belarusguide.representation.places

import com.crownedjester.soft.belarusguide.data.model.PlaceInfo

data class PlacesState(
    val isLoading: Boolean = false,
    val data: List<PlaceInfo>? = emptyList(),
    val error: String? = ""
)