package com.skycom.weathertoday.core.error

sealed interface WeatherError {
    data object Network : WeatherError
    data object NotFound : WeatherError
    data object Unknown : WeatherError
}