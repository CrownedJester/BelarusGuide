package com.crownedjester.soft.belarusguide.representation

data class UiDataState<T>(
    val isLoading: Boolean = false,
    val data: List<T>? = emptyList(),
    val error: String? = ""
)
