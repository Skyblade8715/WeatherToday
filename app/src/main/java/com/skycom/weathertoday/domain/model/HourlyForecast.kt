package com.skycom.weathertoday.domain.model

import java.time.LocalDateTime

data class HourlyForecast(
    val dateTime: LocalDateTime,
    val temperature: Double,
)