package com.skycom.weathertoday.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.weatherDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "weather_today_preferences"
)