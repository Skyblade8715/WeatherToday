package com.skycom.weathertoday.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeocodingResponse(
    val results: List<CityDto>? = null,
)
