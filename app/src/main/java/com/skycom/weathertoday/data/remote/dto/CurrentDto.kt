package com.skycom.weathertoday.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentDto(
    @SerialName("temperature_2m")
    val temperature2m: Double? = null,
    @SerialName("weather_code")
    val weatherCode: Int? = null,
)
