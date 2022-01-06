package com.crownedjester.soft.belarusguide.representation.cities

import com.crownedjester.soft.belarusguide.data.model.CityDto

data class CitiesState(
    val isLoading: Boolean = false,
    val data: List<CityDto>? = emptyList(),
    val error: String? = ""
)
