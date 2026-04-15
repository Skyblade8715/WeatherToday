package com.skycom.weathertoday.presentation.search

import com.skycom.weathertoday.core.error.WeatherError
import com.skycom.weathertoday.core.validation.ValidationReason
import com.skycom.weathertoday.domain.model.City

data class SearchUiState(
    val query: String = "",
    val validationReason: ValidationReason? = null,
    val isLoading: Boolean = false,
    val cities: List<City> = emptyList(),
    val recentCities: List<City> = emptyList(),
    val error: WeatherError? = null,
)