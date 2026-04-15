package com.skycom.weathertoday.navigation

import com.skycom.weathertoday.domain.model.City
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object Details : Screen("details/{cityJson}") {
        fun createRoute(city: City): String {
            return "details/${CityNavSerializer.encode(city)}"
        }
    }
}