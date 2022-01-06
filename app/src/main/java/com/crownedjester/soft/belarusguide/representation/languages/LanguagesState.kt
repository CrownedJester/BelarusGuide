package com.crownedjester.soft.belarusguide.representation.languages

import com.crownedjester.soft.belarusguide.data.model.LanguageDto

data class LanguagesState(
    val isLoading: Boolean = false,
    val data: List<LanguageDto>? = emptyList(),
    val error: String? = ""
)
