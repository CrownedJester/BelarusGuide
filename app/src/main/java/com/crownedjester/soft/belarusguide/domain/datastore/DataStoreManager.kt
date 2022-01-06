package com.crownedjester.soft.belarusguide.domain.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.crownedjester.soft.belarusguide.common.Constants
import com.crownedjester.soft.belarusguide.common.DataPreferences
import com.crownedjester.soft.belarusguide.common.SettingsPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = Constants.DATASTORE_NAME)

class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) :
    DataStoreRepository {

    private val belarusGuideDataStore = appContext.dataStore

    override val isDarkTheme: Flow<Boolean>
        get() = belarusGuideDataStore.getValueAsFlow(
            SettingsPreferences.IS_DARK_THEME_KEY,
            false
        )

    override val currentLangId: Flow<Int>
        get() = belarusGuideDataStore.getValueAsFlow(
            DataPreferences.LANGUAGE_KEY,
            DataPreferences.LANGUAGE_DEFAULT_KEY
        )

    override suspend fun setIsDarkMode(isDarkMode: Boolean) {
        belarusGuideDataStore.setValue(SettingsPreferences.IS_DARK_THEME_KEY, isDarkMode)
    }

    override suspend fun setContentLanguage(langId: Int) {
        belarusGuideDataStore.setValue(DataPreferences.LANGUAGE_KEY, langId)
    }

    private suspend fun <T> DataStore<Preferences>.setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        this.edit { prefs ->
            prefs[key] = value
        }

    }

    private fun <T> DataStore<Preferences>.getValueAsFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return this.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { prefs ->
            prefs[key] ?: defaultValue
        }
    }

}