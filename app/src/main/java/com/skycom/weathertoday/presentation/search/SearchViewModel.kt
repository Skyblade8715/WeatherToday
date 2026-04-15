package com.skycom.weathertoday.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skycom.weathertoday.core.error.WeatherErrorMapper
import com.skycom.weathertoday.domain.repository.WeatherRepository
import com.skycom.weathertoday.core.validation.CityNameValidator
import com.skycom.weathertoday.core.validation.ValidationResult
import com.skycom.weathertoday.domain.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        observeQuery()
        observeRecentCities()
    }

    fun onQueryChange(value: String) {
        val validationResult = CityNameValidator.validate(value)

        _uiState.update {
            it.copy(
                query = value,
                validationReason = when (validationResult) {
                    ValidationResult.Valid -> null
                    is ValidationResult.Invalid -> validationResult.reason
                },
                error = null,
                cities = if (validationResult is ValidationResult.Invalid) emptyList() else it.cities,
            )
        }

        queryFlow.value = value
    }

    fun onCitySelected(city: City) {
        viewModelScope.launch {
            repository.saveRecentCity(city)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearRecentCities()
        }
    }

    @FlowPreview
    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { query ->
                    val trimmed = query.trim()

                    if (trimmed.isBlank()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                cities = emptyList(),
                                error = null,
                                validationReason = null,
                            )
                        }
                        return@collectLatest
                    }

                    when (val validation = CityNameValidator.validate(trimmed)) {
                        ValidationResult.Valid -> {
                            searchCities(trimmed)
                        }
                        is ValidationResult.Invalid -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    cities = emptyList(),
                                    error = null,
                                    validationReason = validation.reason,
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun observeRecentCities() {
        viewModelScope.launch {
            repository.observeRecentCities().collect { recentCities ->
                _uiState.update {
                    it.copy(recentCities = recentCities)
                }
            }
        }
    }

    private suspend fun searchCities(query: String) {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
            )
        }

        runCatching {
            repository.searchCities(query)
        }.onSuccess { cities ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    cities = cities,
                    error = null,
                )
            }
        }.onFailure { throwable ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    cities = emptyList(),
                    error = WeatherErrorMapper.map(throwable),
                )
            }
        }
    }
}