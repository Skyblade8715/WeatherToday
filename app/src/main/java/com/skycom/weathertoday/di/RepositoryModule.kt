package com.skycom.weathertoday.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.skycom.weathertoday.core.dispatcher.IoDispatcher
import com.skycom.weathertoday.data.local.RecentCitiesRepositoryImpl
import com.skycom.weathertoday.data.local.weatherDataStore
import com.skycom.weathertoday.domain.repository.RecentCitiesRepository
import com.skycom.weathertoday.data.remote.api.ForecastApi
import com.skycom.weathertoday.data.remote.api.GeocodingApi
import com.skycom.weathertoday.data.remote.WeatherRepositoryImpl
import com.skycom.weathertoday.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        geocodingApi: GeocodingApi,
        forecastApi: ForecastApi,
        searchHistoryLocalDataSource: RecentCitiesRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): WeatherRepository {
        return WeatherRepositoryImpl(
            geocodingApi = geocodingApi,
            forecastApi = forecastApi,
            recentCitiesRepository = searchHistoryLocalDataSource,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> {
        return context.weatherDataStore
    }

    @Provides
    @Singleton
    fun provideRecentCitiesRepository(
        dataStore: DataStore<Preferences>,
        json: Json,
    ): RecentCitiesRepository {
        return RecentCitiesRepositoryImpl(
            dataStore = dataStore,
            json = json,
        )
    }
}