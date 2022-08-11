package com.crownedjester.soft.belarusguide.representation.languages

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.common.Resource
import com.crownedjester.soft.belarusguide.domain.datastore.DataStoreRepository
import com.crownedjester.soft.belarusguide.domain.use_case.get_languages.GetLanguages
import com.crownedjester.soft.belarusguide.representation.RetryTrigger
import com.crownedjester.soft.belarusguide.representation.retryableFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val getLanguagesUseCase: GetLanguages,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _languagesState = mutableStateOf(LanguagesState())
    val languagesState: State<LanguagesState> = _languagesState

    private val retryTrigger = RetryTrigger()

    init {
        getLanguages()
    }

    fun retryCall() {
        getLanguages()
    }

    private fun getLanguages() {
        retryableFlow(retryTrigger) {
            getLanguagesUseCase().onEach { result ->
                when (result) {
                    is Resource.Loading ->
                        _languagesState.value = LanguagesState(isLoading = true)

                    is Resource.Success ->
                        _languagesState.value = LanguagesState(data = result.data)

                    is Resource.Error ->
                        _languagesState.value = LanguagesState(error = result.message)

                }
            }
        }.launchIn(viewModelScope)
    }

    fun setDataLanguage(langId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.setContentLanguage(langId)
        }
    }
}