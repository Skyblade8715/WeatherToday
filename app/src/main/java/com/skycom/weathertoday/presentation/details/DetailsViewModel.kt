package com.skycom.weathertoday.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skycom.weathertoday.core.error.WeatherError
import com.skycom.weathertoday.core.error.WeatherErrorMapper
import com.skycom.weathertoday.domain.model.City
import com.skycom.weathertoday.domain.repository.WeatherRepository
import com.skycom.weathertoday.navigation.CityNavSerializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: WeatherRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        val encodedCity = savedStateHandle.get<String>("cityJson").orEmpty()

        val city = runCatching {
            CityNavSerializer.decode(encodedCity)
        }.getOrNull()

        if (city == null) {
            _uiState.value = DetailsUiState(
                isLoading = false,
                error = WeatherError.Unknown,
            )
        } else {
            loadForecast(city)
        }
    }

    private fun loadForecast(city: City) {
        viewModelScope.launch {
            runCatching {
                repository.getForecast(city)
            }.onSuccess { forecast ->
                _uiState.value = DetailsUiState(
                    isLoading = false,
                    forecast = forecast,
                )
            }.onFailure { throwable ->
                _uiState.value = DetailsUiState(
                    isLoading = false,
                    error = WeatherErrorMapper.map(throwable),
                )
            }
        }
    }
}