package com.skycom.weathertoday.domain.repository

import com.skycom.weathertoday.domain.model.City
import com.skycom.weathertoday.domain.model.ForecastDetails

interface WeatherRepository {
    suspend fun searchCities(query: String): List<City>
    suspend fun getForecast(city: City): ForecastDetails
    fun observeRecentCities(): kotlinx.coroutines.flow.Flow<List<City>>
    suspend fun saveRecentCity(city: City)
    suspend fun clearRecentCities()
}