package com.skycom.weathertoday.presentation.details

import com.skycom.weathertoday.core.error.WeatherError
import com.skycom.weathertoday.domain.model.ForecastDetails

data class DetailsUiState(
    val isLoading: Boolean = true,
    val forecast: ForecastDetails? = null,
    val error: WeatherError? = null,
)
