package com.crownedjester.soft.belarusguide.representation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.belarusguide.domain.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val dataStore: DataStoreRepository) : ViewModel() {

    fun changeTheme() {
        viewModelScope.launch {
            dataStore.isDarkTheme.collect { isDarkMode ->
                dataStore.setIsDarkMode(!isDarkMode)
            }
        }
    }

}