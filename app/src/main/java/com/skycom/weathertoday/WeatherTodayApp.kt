package com.skycom.weathertoday

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.skycom.weathertoday.navigation.AppNavGraph
import com.skycom.weathertoday.ui.theme.WeatherTodayTheme

@Composable
fun WeatherTodayApp() {
    WeatherTodayTheme {
        AppNavGraph()
    }
}