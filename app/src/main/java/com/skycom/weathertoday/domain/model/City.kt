package com.skycom.weathertoday.domain.model

data class City(
    val id: Long,
    val name: String,
    val country: String,
    val admin1: String?,
    val latitude: Double,
    val longitude: Double,
)

