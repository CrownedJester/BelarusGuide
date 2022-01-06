package com.crownedjester.soft.belarusguide.domain.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val isDarkTheme: Flow<Boolean>
    val currentLangId: Flow<Int>

    suspend fun setIsDarkMode(isDarkMode: Boolean)

    suspend fun setContentLanguage(langId: Int)

}