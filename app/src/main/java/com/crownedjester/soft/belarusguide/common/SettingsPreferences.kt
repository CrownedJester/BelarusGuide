package com.crownedjester.soft.belarusguide.common

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object SettingsPreferences {
    private const val DARK_THEME_KEY_NAME = "pref_dark_theme"
    val IS_DARK_THEME_KEY = booleanPreferencesKey(DARK_THEME_KEY_NAME)

}