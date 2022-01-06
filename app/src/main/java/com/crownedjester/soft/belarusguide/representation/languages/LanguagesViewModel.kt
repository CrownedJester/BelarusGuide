package com.crownedjester.soft.belarusguide.representation.languages

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.domain.datastore.DataStoreRepository
import com.crownedjester.soft.belarusguide.domain.use_case.get_languages.GetLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val getLanguagesUseCase: GetLanguage,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _languagesState = mutableStateOf(LanguagesState())
    val languagesState: State<LanguagesState> = _languagesState

    init {
        getLanguages()
    }

    private fun getLanguages() {
        getLanguagesUseCase().onEach { result ->
            when (result) {
                is Resource.Loading ->
                    _languagesState.value = LanguagesState(isLoading = true)

                is Resource.Success ->
                    _languagesState.value = LanguagesState(data = result.data)

                is Resource.Error ->
                    _languagesState.value = LanguagesState(error = result.message)

            }
        }.launchIn(viewModelScope)
    }

    fun setDataLanguage(langId: Int) = viewModelScope.launch {
        dataStoreRepository.setContentLanguage(langId)
    }
}