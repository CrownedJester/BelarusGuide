package com.crownedjester.soft.belarusguide.common

import androidx.datastore.preferences.core.intPreferencesKey

object DataPreferences {

    const val LANGUAGE_DEFAULT_KEY = 1
    private const val LANGUAGE_KEY_NAME = "current_language"
    val LANGUAGE_KEY = intPreferencesKey(LANGUAGE_KEY_NAME)


}