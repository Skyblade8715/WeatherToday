package com.skycom.weathertoday.data.remote.dto

import com.skycom.weathertoday.domain.model.City
import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    val id: Long,
    val name: String,
    val country: String,
    val admin1: String? = null,
    val latitude: Double,
    val longitude: Double,
) {
    fun toDomain(): City = City(
        id = id,
        name = name,
        country = country,
        admin1 = admin1,
        latitude = latitude,
        longitude = longitude,
    )
}