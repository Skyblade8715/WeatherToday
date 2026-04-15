package com.skycom.weathertoday.domain.model

data class ForecastDetails(
    val cityName: String,
    val currentTemperature: Double?,
    val currentWeatherCode: Int?,
    val hourly: List<HourlyForecast>,
)