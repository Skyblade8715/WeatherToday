@file:OptIn(ExperimentalMaterial3Api::class)

package com.skycom.weathertoday.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.skycom.weathertoday.presentation.details.DetailsScreen
import com.skycom.weathertoday.presentation.details.DetailsViewModel
import com.skycom.weathertoday.presentation.search.SearchScreen
import com.skycom.weathertoday.presentation.search.SearchViewModel


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
    ) {
        composable(Screen.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()

            val uiState = viewModel.uiState.collectAsStateWithLifecycle()

            SearchScreen(
                uiState = uiState.value,
                onQueryChange = viewModel::onQueryChange,
                onCityClick = { city ->
                    viewModel.onCitySelected(city)
                    navController.navigate(Screen.Details.createRoute(city))
                },
                onClearHistoryClick = viewModel::clearHistory,
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument("cityJson") {
                    type = NavType.StringType
                },
            ),
        ) {
            val viewModel: DetailsViewModel = hiltViewModel()

            val uiState = viewModel.uiState.collectAsStateWithLifecycle()

            DetailsScreen(
                uiState = uiState.value,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}