package com.crownedjester.soft.belarusguide.representation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.domain.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(private val dataStore: DataStoreRepository) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    init {
        getTheme()
    }

    private fun getTheme() {
        viewModelScope.launch {
            dataStore.isDarkTheme.collectLatest { mode ->
                _isDarkMode.emit(mode)
            }
        }
    }

    fun changeTheme() {
        viewModelScope.launch {
            dataStore.setIsDarkMode(!_isDarkMode.value)
        }
    }

}