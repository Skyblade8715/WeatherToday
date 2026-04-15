package com.skycom.weathertoday.presentation.common

import androidx.compose.ui.graphics.Color

object TemperatureColorMapper {

    fun map(temp: Double): Color {
        return when {
            temp < 10.0 -> Color.Blue
            temp <= 20.0 -> Color.Black
            else -> Color.Red
        }
    }
}