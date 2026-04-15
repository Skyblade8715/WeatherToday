package com.skycom.weathertoday.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val current: CurrentDto? = null,
    val hourly: HourlyDto? = null,
)
