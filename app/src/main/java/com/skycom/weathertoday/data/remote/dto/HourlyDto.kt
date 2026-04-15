package com.skycom.weathertoday.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyDto(
    val time: List<String> = emptyList(),
    @SerialName("temperature_2m")
    val temperature2m: List<Double> = emptyList(),
)