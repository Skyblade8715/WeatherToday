package com.skycom.weathertoday.core.error

object WeatherErrorMapper {

    fun map(throwable: Throwable): WeatherError {
        return when (throwable) {
            is java.io.IOException -> WeatherError.Network
            is retrofit2.HttpException -> {
                when (throwable.code()) {
                    404 -> WeatherError.NotFound
                    else -> WeatherError.Unknown
                }
            }
            else -> WeatherError.Unknown
        }
    }
}