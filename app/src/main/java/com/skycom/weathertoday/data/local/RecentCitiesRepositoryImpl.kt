package com.skycom.weathertoday.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.skycom.weathertoday.data.local.dto.StoredCityDto
import com.skycom.weathertoday.domain.model.City
import com.skycom.weathertoday.domain.repository.RecentCitiesRepository
import kotlinx.serialization.json.Json
import kotlin.collections.filterNot
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.take
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecentCitiesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) : RecentCitiesRepository {

    private companion object {
        val RECENT_CITIES_KEY = stringPreferencesKey("recent_cities")
        const val MAX_HISTORY_SIZE = 8
    }

    override fun observeRecentCities(): Flow<List<City>> {
        return dataStore.data.map { preferences ->
            val raw = preferences[RECENT_CITIES_KEY].orEmpty()
            if (raw.isBlank()) {
                emptyList()
            } else {
                runCatching {
                    json.decodeFromString<List<StoredCityDto>>(raw).map { it.toDomain() }
                }.getOrDefault(emptyList())
            }
        }
    }

    override suspend fun saveCity(city: City) {
        dataStore.edit { preferences ->
            val current = preferences[RECENT_CITIES_KEY].orEmpty()

            val decoded = if (current.isBlank()) {
                emptyList()
            } else {
                runCatching {
                    json.decodeFromString<List<StoredCityDto>>(current)
                }.getOrDefault(emptyList())
            }

            val updated = buildList {
                add(StoredCityDto.from(city))
                decoded.filterNot {
                        it.id == city.id && it.latitude == city.latitude && it.longitude == city.longitude
                    }.take(MAX_HISTORY_SIZE - 1).forEach(::add)
            }

            preferences[RECENT_CITIES_KEY] = json.encodeToString(updated)
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(RECENT_CITIES_KEY)
        }
    }
}