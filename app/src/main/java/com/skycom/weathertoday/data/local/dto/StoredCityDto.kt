package com.skycom.weathertoday.data.local.dto

import com.skycom.weathertoday.domain.model.City
import kotlinx.serialization.Serializable

@Serializable
data class StoredCityDto(
    val id: Long,
    val name: String,
    val country: String,
    val admin1: String?,
    val latitude: Double,
    val longitude: Double,
) {
    fun toDomain(): City {
        return City(
            id = id,
            name = name,
            country = country,
            admin1 = admin1,
            latitude = latitude,
            longitude = longitude,
        )
    }

    companion object {
        fun from(city: City): StoredCityDto {
            return StoredCityDto(
                id = city.id,
                name = city.name,
                country = city.country,
                admin1 = city.admin1,
                latitude = city.latitude,
                longitude = city.longitude,
            )
        }
    }
}