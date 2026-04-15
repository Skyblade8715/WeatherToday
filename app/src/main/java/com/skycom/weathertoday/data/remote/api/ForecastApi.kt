package com.skycom.weathertoday.data.remote.api

import com.skycom.weathertoday.data.remote.dto.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,weather_code",
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("forecast_days") forecastDays: Int = 2,
    ): ForecastResponse
}