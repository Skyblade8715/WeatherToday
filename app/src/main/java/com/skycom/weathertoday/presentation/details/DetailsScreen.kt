package com.skycom.weathertoday.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skycom.weathertoday.R
import com.skycom.weathertoday.core.error.WeatherError
import com.skycom.weathertoday.domain.model.ForecastDetails
import com.skycom.weathertoday.domain.model.HourlyForecast
import com.skycom.weathertoday.presentation.common.ErrorState
import com.skycom.weathertoday.presentation.common.TemperatureColorMapper
import com.skycom.weathertoday.ui.theme.WeatherTodayTheme
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    uiState: DetailsUiState,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.details_title))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    ErrorState(
                        message = when (uiState.error) {
                            WeatherError.Network -> stringResource(R.string.error_network)
                            WeatherError.NotFound -> stringResource(R.string.error_not_found)
                            WeatherError.Unknown -> stringResource(R.string.error_unknown)
                        },
                    )
                }
            }

            uiState.forecast != null -> {
                val forecast = uiState.forecast

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                            ) {
                                Text(
                                    text = forecast.cityName,
                                    style = MaterialTheme.typography.headlineSmall,
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                forecast.currentTemperature?.let { temp ->
                                    Text(
                                        text = stringResource(
                                            R.string.temperature_value,
                                            temp,
                                        ),
                                        color = TemperatureColorMapper.map(temp),
                                        style = MaterialTheme.typography.displaySmall,
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = stringResource(R.string.hourly_forecast_title),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    items(
                        items = forecast.hourly,
                        key = { item -> item.dateTime.toString() },
                    ) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = TimeFormatter.formatForForecastRow(item.dateTime),
                                )
                                Text(
                                    text = stringResource(
                                        R.string.temperature_value,
                                        item.temperature,
                                    ),
                                    color = TemperatureColorMapper.map(item.temperature),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun DetailsScreenPreview() {
    WeatherTodayTheme {
        DetailsScreen(
            uiState = DetailsUiState(
                isLoading = false,
                forecast = ForecastDetails(
                    cityName = "Warsaw, Mazowieckie, Poland",
                    currentTemperature = 18.5,
                    currentWeatherCode = 1,
                    hourly = listOf(
                        HourlyForecast(LocalDateTime.of(2026, 4, 15, 21, 0), 5.5),
                        HourlyForecast(LocalDateTime.of(2026, 4, 15, 22, 0), 19.1),
                        HourlyForecast(LocalDateTime.of(2026, 4, 15, 23, 0), 20.3),
                    ),
                ),
            ),
            onBackClick = {},
        )
    }
}