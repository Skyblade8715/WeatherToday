package com.skycom.weathertoday.domain.repository

import com.skycom.weathertoday.domain.model.City
import kotlinx.coroutines.flow.Flow

interface RecentCitiesRepository {
    fun observeRecentCities(): Flow<List<City>>
    suspend fun saveCity(city: City)
    suspend fun clear()
}