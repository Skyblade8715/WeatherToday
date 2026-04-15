@file:OptIn(ExperimentalMaterial3Api::class)

package com.skycom.weathertoday.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skycom.weathertoday.core.error.WeatherError
import com.skycom.weathertoday.core.validation.ValidationReason
import com.skycom.weathertoday.domain.model.City
import com.skycom.weathertoday.presentation.common.EmptyState
import com.skycom.weathertoday.presentation.common.ErrorState
import com.skycom.weathertoday.ui.theme.WeatherTodayTheme


@ExperimentalMaterial3Api
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onCityClick: (City) -> Unit,
    onClearHistoryClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(com.skycom.weathertoday.R.string.search_title),
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = onQueryChange,
                label = {
                    Text(
                        text = stringResource(com.skycom.weathertoday.R.string.search_input_label),
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(com.skycom.weathertoday.R.string.search_input_placeholder),
                    )
                },
                isError = uiState.validationReason != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    uiState.validationReason?.let { reason ->
                        Text(
                            text = when (reason) {
                                ValidationReason.TOO_SHORT ->
                                    stringResource(com.skycom.weathertoday.R.string.validation_too_short)

                                ValidationReason.INVALID_CHARACTERS ->
                                    stringResource(com.skycom.weathertoday.R.string.validation_invalid_characters)
                            },
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )

            Spacer(
                modifier = Modifier.height(16.dp),
            )

            if (uiState.query.isBlank() && uiState.recentCities.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(com.skycom.weathertoday.R.string.recent_searches_title),
                        style = MaterialTheme.typography.titleMedium,
                    )

                    androidx.compose.material3.TextButton(
                        onClick = onClearHistoryClick,
                    ) {
                        Text(
                            text = stringResource(com.skycom.weathertoday.R.string.clear_history),
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp),
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = uiState.recentCities,
                        key = { city -> "recent_${city.id}_${city.latitude}_${city.longitude}" },
                    ) { city ->
                        CityRow(
                            city = city,
                            onClick = { onCityClick(city) },
                        )
                    }
                }
            } else {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator()
                    }

                    uiState.error != null -> {
                        ErrorState(
                            message = when (uiState.error) {
                                WeatherError.Network ->
                                    stringResource(com.skycom.weathertoday.R.string.error_network)

                                WeatherError.NotFound ->
                                    stringResource(com.skycom.weathertoday.R.string.error_not_found)

                                WeatherError.Unknown ->
                                    stringResource(com.skycom.weathertoday.R.string.error_unknown)
                            },
                        )
                    }

                    uiState.query.isNotBlank() &&
                            uiState.validationReason == null &&
                            uiState.cities.isEmpty() -> {
                        EmptyState(
                            message = stringResource(com.skycom.weathertoday.R.string.empty_search_results),
                        )
                    }

                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(
                                items = uiState.cities,
                                key = { city -> "${city.id}_${city.latitude}_${city.longitude}" },
                            ) { city ->
                                CityRow(
                                    city = city,
                                    onClick = { onCityClick(city) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchScreenResultsPreview() {
    WeatherTodayTheme {
        SearchScreen(
            uiState = SearchUiState(
                query = "War",
                cities = listOf(
                    City(1, "Warsaw", "Poland", "Mazowieckie", 3.2, 1.3),
                    City(2, "Warwick", "UK", null, 2.3, 2.1),
                ),
            ),
            onQueryChange = {},
            onCityClick = {},
            onClearHistoryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenValidationPreview() {
    WeatherTodayTheme {
        SearchScreen(
            uiState = SearchUiState(
                query = "12",
                validationReason = ValidationReason.INVALID_CHARACTERS,
            ),
            onQueryChange = {},
            onCityClick = {},
            onClearHistoryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenRecentPreview() {
    WeatherTodayTheme {
        SearchScreen(
            uiState = SearchUiState(
                recentCities = listOf(
                    City(1, "Warsaw", "Poland", "Mazowieckie", 3.2, 1.3),
                    City(2, "Kraków", "Poland", "Małopolskie", 2.3, 2.1),
                ),
            ),
            onQueryChange = {},
            onCityClick = {},
            onClearHistoryClick = {},
        )
    }
}