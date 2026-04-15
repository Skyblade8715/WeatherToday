package com.skycom.weathertoday.data.remote

import com.skycom.weathertoday.core.dispatcher.IoDispatcher
import com.skycom.weathertoday.data.remote.api.ForecastApi
import com.skycom.weathertoday.data.remote.api.GeocodingApi
import com.skycom.weathertoday.domain.model.City
import com.skycom.weathertoday.domain.model.ForecastDetails
import com.skycom.weathertoday.domain.model.HourlyForecast
import com.skycom.weathertoday.domain.repository.RecentCitiesRepository
import com.skycom.weathertoday.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class WeatherRepositoryImpl(
    private val geocodingApi: GeocodingApi,
    private val forecastApi: ForecastApi,
    private val recentCitiesRepository: RecentCitiesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WeatherRepository {

    override suspend fun searchCities(query: String): List<City> =
        withContext(ioDispatcher) {
            geocodingApi.searchCities(name = query)
                .results
                .orEmpty()
                .map { it.toDomain() }
        }

    override suspend fun getForecast(city: City): ForecastDetails =
        withContext(ioDispatcher) {
            val response = forecastApi.getForecast(
                latitude = city.latitude,
                longitude = city.longitude,
                forecastDays = 2,
            )

            val threshold = nextFullHour(LocalDateTime.now())

            val hourly = response.hourly
                ?.time
                .orEmpty()
                .zip(response.hourly?.temperature2m.orEmpty())
                .mapNotNull { (time, temperature) ->
                    runCatching {
                        HourlyForecast(
                            dateTime = LocalDateTime.parse(time),
                            temperature = temperature,
                        )
                    }.getOrNull()
                }
                .filter { it.dateTime >= threshold }
                .sortedBy { it.dateTime }
                .take(12)

            ForecastDetails(
                cityName = buildString {
                    append(city.name)
                    city.admin1?.let {
                        append(", ")
                        append(it)
                    }
                    append(", ")
                    append(city.country)
                },
                currentTemperature = response.current?.temperature2m,
                currentWeatherCode = response.current?.weatherCode,
                hourly = hourly,
            )
        }


    override fun observeRecentCities(): Flow<List<City>> {
        return recentCitiesRepository.observeRecentCities()
    }

    override suspend fun saveRecentCity(city: City) {
        withContext(ioDispatcher) {
            recentCitiesRepository.saveCity(city)
        }
    }

    override suspend fun clearRecentCities() {
        withContext(ioDispatcher) {
            recentCitiesRepository.clear()
        }
    }

    private fun nextFullHour(now: LocalDateTime): LocalDateTime {
        return now
            .plusHours(1)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
    }
}