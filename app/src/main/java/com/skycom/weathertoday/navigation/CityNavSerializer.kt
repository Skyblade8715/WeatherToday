package com.skycom.weathertoday.navigation

import android.net.Uri
import com.skycom.weathertoday.domain.model.City
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object CityNavSerializer {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Serializable
    private data class NavCity(
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
            fun from(city: City): NavCity {
                return NavCity(
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

    fun encode(city: City): String {
        val rawJson = json.encodeToString(NavCity.from(city))
        return Uri.encode(rawJson)
    }

    fun decode(encoded: String): City {
        val rawJson = Uri.decode(encoded)
        return json.decodeFromString<NavCity>(rawJson).toDomain()
    }
}